import java.io.PrintWriter
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils

case class Main(user: String, db: String, tableMysql: String, tableOut: String, format: String)
    extends MysqlAware(db, user)
    with SparkAware {

  val bunchSize = 10240

  val rs = queryTableNotIntoRAM(tableMysql)
  val schema = JdbcUtils.getSchema(rs, dialect)
  val data = JdbcUtils.resultSetToRows(rs, schema).grouped(bunchSize) // as Iterator[Row]#GroupedIterator[Row]

  val schemaFile = tableOut + ".schema"
  println(s"Copy data from MySQL ($tableMysql) into filesystem ($tableOut)")
  new PrintWriter(schemaFile) { write(schema.prettyJson); close } // save data schema into file (avsc)

  data.zipWithIndex.foreach {
    case (bunch, idx) => // bunch: Seq[Row]

      // Print memory usage
      val runtime = Runtime.getRuntime
      val used = (runtime.totalMemory - runtime.freeMemory) / 1024 / 1024
      println(s"Bunch #${idx + 1}. Memory used $used MB")

      // convert bunch into DataFrame
      val rdd = sc.parallelize(bunch)
      val df = spark.createDataFrame(rdd, schema)

      df.write
        .format(format)
        .mode(idx match {
          case 0 => SaveMode.Overwrite
          case _ => SaveMode.Append
        })
        .save(tableOut)

      // important! Otherwise memory leaks
      df.unpersist()
      rdd.unpersist()

      System.gc()
  }

}

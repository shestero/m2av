import org.apache.hadoop.conf.Configuration
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

trait SparkAware {
  // Spark configuration:
  val conf = new SparkConf()
    .setMaster("local[*]")
    .setAppName(StartApp.app)

  val spark: SparkSession = SparkSession
    .builder()
    .config(conf)
    .getOrCreate()

  val sc: SparkContext = spark.sparkContext
  sc.setLogLevel("ERROR")

  // Enable access to file systems
  val hadoopConfig: Configuration = spark.sparkContext.hadoopConfiguration
  hadoopConfig.set(
    "fs.hdfs.impl",
    classOf[org.apache.hadoop.hdfs.DistributedFileSystem].getName)
  hadoopConfig.set("fs.file.impl",
                   classOf[org.apache.hadoop.fs.LocalFileSystem].getName)

}

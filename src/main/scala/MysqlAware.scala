import java.sql.{Connection, DriverManager, ResultSet, Statement}
import java.util.TimeZone

import org.apache.spark.sql.jdbc.JdbcDialects

class MysqlAware(db: String,
                 user: String,
                 pwd: String = "",
                 host: String = "localhost",
                 port: Int = 3306) {
// Note: socks mapping: ssh user@server -L 3307:127.0.0.1:3306 -N

  val parameters = Map(
    "useUnicode" -> "true",
    "characterEncoding" -> "UTF-8",
    "zeroDateTimeBehavior" -> "convertToNull",
    "serverTimezone" -> TimeZone.getDefault().getID(),
    "useCursorFetch" -> "true"
  )
  // ?useCursorFetch=true  to avoid early fetching all rows, more details on
  // https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-implementation-notes.html

  val url = s"jdbc:mysql://$host:$port/$db?" + parameters
    .map { case (k, v) => k + "=" + v }
    .mkString("&")

  val dialect = JdbcDialects.get(url)

  val dbc: Connection = DriverManager.getConnection(url, user, pwd)

  def queryTableNotIntoRAM(tableName: String): ResultSet = {
    val st: Statement =
      dbc.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                          ResultSet.CONCUR_READ_ONLY)
    st.setFetchSize(Integer.MIN_VALUE)

    val sql = s"SELECT * FROM $tableName" // TODO: beware table name injection!

    st.executeQuery(sql)
  }
}

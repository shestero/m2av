object StartApp extends App {
  val app = "m2av"
  println(s"HI\t$app")

  val params = Seq(
    "MySQL login (use empty password)",
    "MySQL database",
    "MySQL source table name",
    "target file name (with extension)",
    "output format (avro, orc, ...)"
  )

  if (args.length != params.size) {
    println(s"Expect ${params.size} parameters:")
    println(
      (Stream.from(1) zip params)
        .map { case (n, s) => s"\t$n) $s" }
        .mkString("\n"))
    println
  } else {
    Class.forName("com.mysql.cj.jdbc.Driver").newInstance
    //Class.forName("com.mysql.jdbc.Driver")

    Main(args(0), args(1), args(2), args(3), args(4))
  }

  println(s"BYE\t$app")
}

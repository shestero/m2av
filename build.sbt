name := "m2av"

version := "1.0"

scalaVersion := "2.12.12"
val sparkVersion = "3.0.1"

libraryDependencies ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, major)) if major <= 12 =>
      Seq()
    case _ =>
      Seq("org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0")
  }
}


libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-avro" % sparkVersion
// libraryDependencies += "org.apache.parquet" % "parquet-hadoop" % "1.11.1"

libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.+"

assemblyMergeStrategy in assembly := {
  //case PathList("org", "apache", "arrow", xs @ _*) => MergeStrategy.last
  case PathList("javax", "inject", xs @ _*) => MergeStrategy.last
  case PathList("org","aopalliance", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  //case "UnusedStubClass.class" => MergeStrategy.last // spark
  case PathList("org", "apache", "commons", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "glassfish", xs @ _*) => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case "git.properties" => MergeStrategy.last
  case "module-info.class" => MergeStrategy.last
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

mainClass in (Compile, run) := Some("StartApp")

logLevel := Level.Warn
resolvers += Resolver.url(
  "idio",
  url("http://dl.bintray.com/idio/sbt-plugins")
)(Resolver.ivyStylePatterns)
//addSbtPlugin("org.scala-sbt" % "sbt-duplicates-finder" % "0.7.0")

enablePlugins(AssemblyPlugin)
//unmanagedJars in Compile ++= Seq( file("lib_managed/jars/mysql/mysql-connector-java/mysql-connector-java-8.0.20.jar") ) // force

// https://stackoverflow.com/questions/19584686/java-lang-noclassdeffounderror-while-running-jar-from-scala
retrieveManaged := true
// !! bug: https://github.com/sbt/sbt/issues/5078
// at SBT 1.3.0 - 1.3.12
useCoursier := false

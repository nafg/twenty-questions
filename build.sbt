name := "twenty-questions"

version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.1",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.h2database" % "h2" % "1.4.196"
)

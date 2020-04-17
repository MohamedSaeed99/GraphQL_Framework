name := "maxwell_dausch_project"

version := "0.1"

scalaVersion := "2.13.1"

mainClass in (Compile, run) := Some("com.cs474.Main")

// slf4j 1.7.30
libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.5",
                            "org.slf4j" % "slf4j-simple" % "1.7.5")

// logback 1.2.3
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

//Typesafe 1.0.2
libraryDependencies += "com.typesafe" % "config" % "1.0.2"

//HttpBuilder
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.12"



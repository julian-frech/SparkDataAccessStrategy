ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := "2.11.12"

val sparkVersion = "2.4.5"

lazy val root = (project in file("."))
  .settings(
    name := "DesignSamples",
    idePackagePrefix := Some("org.julian.frech")
  )

resolvers ++= Seq(
  "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven",
  "Typesafe Simple Repository" at "https://repo.typesafe.com/typesafe/simple/maven-releases",
  "MavenRepository" at "https://mvnrepository.com"
)


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion,
  "org.apache.spark" %% "spark-avro" % sparkVersion,
  "com.sksamuel.avro4s" %% "avro4s-core" % "2.0.2",
// logging
  "org.apache.logging.log4j" % "log4j-api" % "2.20.0",
  "org.apache.logging.log4j" % "log4j-core" % "2.20.0"
)
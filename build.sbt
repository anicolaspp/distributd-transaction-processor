name := "distributd-transaction-processor"

version := "1.0"

scalaVersion := "2.11.8"


//libraryDependencies += "org.scalactic" %% "scalactic" % "2.2.6"
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % "2.4.7",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.4.7",
  "com.github.nscala-time" %% "nscala-time" % "2.12.0",
  "com.typesafe.akka" % "akka-cluster-metrics_2.11" % "2.4.6",

  "com.typesafe.akka" %% "akka-actor" % "2.4.7",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.7"
)


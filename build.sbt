name := "distributd-transaction-processor"

version := "1.0"

scalaVersion := "2.11.8"

val kamonVersion = "0.6.0"


libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % "2.4.7",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.4.7",
  "com.github.nscala-time" %% "nscala-time" % "2.12.0",
  "com.typesafe.akka" % "akka-cluster-metrics_2.11" % "2.4.6",

  "com.typesafe.akka" %% "akka-actor" % "2.4.7",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.7",
  "com.typesafe.akka" %% "akka-cluster" % "2.4.7",
  "com.typesafe.akka" %% "akka-cluster-tools" % "2.4.7",

  "io.kamon" %% "kamon-core" % kamonVersion,
  "io.kamon" %% "kamon-akka" % kamonVersion,
  "io.kamon" %% "kamon-statsd" % kamonVersion,
  "io.kamon" %% "kamon-log-reporter" % kamonVersion,
  "io.kamon" %% "kamon-system-metrics" % kamonVersion,
  "org.aspectj" % "aspectjweaver" % "1.8.5"
)

enablePlugins(JavaServerAppPackaging)

mainClass in Compile := Some("com.nico.simulation.persistence.app")
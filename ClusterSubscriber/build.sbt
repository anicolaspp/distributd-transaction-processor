name := "subscriber"

val akkaVersion = "2.4.7"
val kamonVersion = "0.6.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" % "akka-cluster-metrics_2.11" % akkaVersion,

  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-distributed-data-experimental" % akkaVersion,

  "io.kamon" %% "kamon-core" % kamonVersion,
  "io.kamon" %% "kamon-akka" % kamonVersion,
  "io.kamon" %% "kamon-statsd" % kamonVersion,
  "io.kamon" %% "kamon-log-reporter" % kamonVersion,
  "io.kamon" %% "kamon-system-metrics" % kamonVersion,
  "org.aspectj" % "aspectjweaver" % "1.8.5"
)

dockerBaseImage := "java"
enablePlugins(JavaAppPackaging)
enablePlugins(DockerComposePlugin)

dockerImageCreationTask := (publishLocal in Docker).value

//mainClass in Compile := Some("com.nico.simulation.persistence.app")


aspectjSettings

javaOptions <++= AspectjKeys.weaverOptions in Aspectj

// when you call "sbt run" aspectj weaving kicks in
fork in run := true


mainClass in (Compile, run) := Some("com.nico.DistributedSubscriber.DistributedSubscriberApp")

parallelExecution in myRun := true

lazy val myRun = inputKey[Unit]("custom run")

myRun := Def.inputTaskDyn {
  val args = Def.spaceDelimited("").parsed.toList.reverse

  val values = args.mkString(" ")

  (run in Compile).toTask(" com.nico.DistributedSubscriber.DistributedSubscriberApp 9080 10")
  (run in Compile).toTask(" com.nico.DistributedSubscriber.DistributedSubscriberApp 9081 10")
}.evaluated


import NativePackagerHelper._

name := "http"

val akkaVersion = "2.4.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion
)

enablePlugins(JavaServerAppPackaging)
enablePlugins(DockerComposePlugin)

mappings in Universal ++= contentOf("src/main/resources")


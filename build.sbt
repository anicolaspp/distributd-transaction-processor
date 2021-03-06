name := "distributd-transaction-processor"

version in Global := "1.0"

scalaVersion in Global := "2.11.8"


lazy val runner = project.in(file("ClusterRunner"))
  .aggregate (
    core,
    http,
    tests,
    actors,
    clusterPublisher,
    clusterSubscriber,
    clusterSingleton
  )
  .dependsOn (
    core,
    http,
    tests,
    actors,
    clusterPublisher,
    clusterSubscriber,
    clusterSingleton
  )




lazy val clusterSingleton = project.in(file("ClusterSingleton")) dependsOn(core, actors)
lazy val clusterSubscriber = project.in(file("ClusterSubscriber")) dependsOn(core, actors)
lazy val clusterPublisher = project.in(file("ClusterPublisher")) dependsOn (core, actors)

lazy val actors = project.in(file("actors")) dependsOn core
lazy val tests = project.in(file("tests")) dependsOn(core, http, actors)
lazy val http = project.in(file("http")) dependsOn core
lazy val core = project.in(file("core"))


Keys.fork in run := true

parallelExecution in test := false

coverageEnabled := true

//addCommandAlias("deploySubscribers", ";clusterSubscriber/compile;clusterSubscriber/run")



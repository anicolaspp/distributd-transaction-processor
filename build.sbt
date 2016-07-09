name := "distributd-transaction-processor"

version in Global := "1.0"

scalaVersion in Global := "2.11.8"


lazy val processor = project.in(file(".")) aggregate (core, http, tests)

lazy val tests = project.in(file("tests")) dependsOn(core, http)
lazy val http = project.in(file("http")) dependsOn core
lazy val core = project.in(file("core"))

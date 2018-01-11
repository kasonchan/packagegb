import pgb.PgbPlugin

name := "pgb-it"

version := "0.1"

scalaVersion := "2.12.4"

lazy val plugins = (project in file("."))
  .enablePlugins(PgbPlugin)

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "4.0.2" % "test")

scalacOptions in Test ++= Seq("-Yrangepos")

parallelExecution in ThisBuild := false

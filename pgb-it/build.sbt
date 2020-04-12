import pgb.PgbPlugin

name := "pgb-it"

version := "0.0.3"

scalaVersion := "2.12.10"

lazy val plugins = (project in file("."))
  .enablePlugins(PgbPlugin)

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "4.8.3" % "test")

scalacOptions in Test ++= Seq("-Yrangepos")

gatlingVersion := "3.3.1"

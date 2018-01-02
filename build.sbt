val name = "pgb"

lazy val buildSettings = Seq(
  organization := "com.kasonchan",
  version := "0.0.1",
  scalaVersion := "2.12.4"
)

lazy val compilerOptions = Seq(
  "-encoding",
  "UTF-8",
  "-target:jvm-1.8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:implicitConversions",
  "-language:postfixOps"
)

val testDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4"
)

val baseSettings = Seq(
  libraryDependencies ++= testDependencies.map(_ % "test"),
  scalacOptions in(Compile, console) := compilerOptions,
  sbtPlugin := true
)

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := true
)

lazy val allSettings = baseSettings ++ buildSettings ++ noPublishSettings

lazy val pgb = (project in file("."))
  .settings(
    allSettings
  )

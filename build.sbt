val name = "pgb"

lazy val buildSettings = Seq(
  organization := "com.kasonchan",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.12.4"
)

lazy val compilerOptions = Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:implicitConversions",
  "-language:postfixOps"
)

val testDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.0.4",
  "org.scalatest" %% "scalatest" % "3.0.4" % "it,test",
  "org.specs2" %% "specs2-core" % "4.0.2" % "it,test"
)

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

val baseSettings = Seq(
  sbtPlugin := true,
  libraryDependencies ++= testDependencies,
  scalacOptions in(Compile, console) := compilerOptions,
  compileScalastyle := scalastyle.in(Compile).toTask("").value,
  (compile in Compile) := ((compile in Compile) dependsOn compileScalastyle).value
)

lazy val allSettings = baseSettings ++ buildSettings

lazy val pgb = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    allSettings
  )

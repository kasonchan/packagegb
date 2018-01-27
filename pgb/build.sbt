val name = "pgb"

lazy val buildSettings = Seq(
  organization := "com.kasonchan",
  version := "0.0.1",
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
  (compile in Compile) := ((compile in Compile) dependsOn compileScalastyle).value,
  parallelExecution in ThisBuild := false
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  publishArtifact in Test := false,
  licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
  homepage := Some(url("https://github.com/kasonchan/pgb")),
  autoAPIMappings := true,
  apiURL := None,
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/kasonchan/pgb"),
      "scm:git:git@github.com:kasonchan/pgb.git"
    )
  ),
  developers := List(
    Developer(
      id = "kasonchan",
      name = "Kason Chan",
      email = "kasonl.chan@gmail.com",
      url = url("https://github.com/kasonchan/")
    )
  ),
  pomExtra :=
    <developers>
      <developer>
        <id>kasonchan</id>
        <name>Kason Chan</name>
        <url>https://github.com/kasonchan</url>
      </developer>
    </developers>
      <licenses>
        <license>
          <name>MIT</name>
          <url>https://opensource.org/licenses/MIT</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
)

lazy val allSettings = baseSettings ++ buildSettings ++ publishSettings

lazy val pgb = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    allSettings
  )

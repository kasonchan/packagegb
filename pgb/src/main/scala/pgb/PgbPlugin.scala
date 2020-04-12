package pgb

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

import scala.sys.process._

/**
  * @author kasonchan
  * @since Jan-2018
  */
object PgbPlugin extends AutoPlugin {
  object autoImport {
    val deploy: TaskKey[Int] =
      taskKey[Int]("produces a zipped project bundled with Gatling")
    val gatlingVersion: SettingKey[String] =
      settingKey[String]("sets Gatling bundle version")

    def baseDeploySettings: Seq[Def.Setting[_]] =
      Seq(
        deploy := {
          val projectName = s"${name.value}-${version.value}"
          Pgb.deployGB()
          state.value.log.info(s"Project $projectName")
          state.value.log.info(s"Gatling bundle ${gatlingVersion.value}")
          s"./${Pgb.scriptName} $projectName ${gatlingVersion.value}".!
        }
      )
  }

  import autoImport._

  override def requires: JvmPlugin.type = sbt.plugins.JvmPlugin
  override def trigger: PluginTrigger = allRequirements

  override lazy val projectSettings: Seq[Def.Setting[_]] =
    inConfig(Compile)(baseDeploySettings) ++ Seq(
      Keys.commands ++= Seq(PgbCommand.download)
    )
}

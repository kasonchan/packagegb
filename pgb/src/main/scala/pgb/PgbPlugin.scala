package pgb

import sbt.Keys._
import sbt.plugins.JvmPlugin
import sbt.{AutoPlugin, Def, Keys, taskKey, _}

import scala.sys.process._

/**
  * @author kasonchan
  * @since Jan-2018
  */
object PgbPlugin extends AutoPlugin {
  object autoImport {
    val deploy: TaskKey[Int] =
      taskKey[Int]("Produces a zipped project bundled with Gatling.")

    def baseDeploySettings: Seq[Def.Setting[_]] =
      Seq(deploy := {
        val newBundleName = s"${name.value}-${version.value}"
        Pgb.deployGB()
        state.value.log.info(s"$newBundleName")
        state.value.log.info(s"${Pgb.gatlingVersion}")
        s"./${Pgb.scriptName} $newBundleName ${Pgb.gatlingVersion}".!
      })
  }

  import autoImport._

  override def requires: JvmPlugin.type = sbt.plugins.JvmPlugin
  override def trigger: PluginTrigger = allRequirements

  override lazy val projectSettings: Seq[Def.Setting[_]] = inConfig(Compile)(
    baseDeploySettings) ++ Seq(
    Keys.commands ++= Seq(PgbCommand.download)
  )
}

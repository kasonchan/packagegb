package kasonchan

import sbt.{AutoPlugin, Def, taskKey}

/**
  * @author kasonchan
  * @since Jan-2018
  */
object PgbPlugin extends AutoPlugin {

  import autoImport._

  object autoImport {
    val dlgb = taskKey[Unit]("Download Gatling Bundle")
  }

  override lazy val projectSettings = Seq(
    dlgb := dlgbTask.value
  )

  lazy val dlgbTask =
    Def.task {
      Pgb.dlgb
    }

}

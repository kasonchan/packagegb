package kasonchan

import sbt.{Def, _}

import scala.util.Try

/**
  * @author kasonchan
  * @since Jan-2018
  */
object PgbPlugin extends AutoPlugin {

  import autoImport._

  override lazy val projectSettings = Seq(
    dlgb
  )

  object autoImport {
    val dlgb = taskKey[Try[Int]]("Download Gatling Bundle")
  }

  lazy val dlgbTask: Def.Initialize[Task[Try[Int]]] =
    Def.task {
      Pgb.dlgb
    }

}

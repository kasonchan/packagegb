package pgb

import sbt.{AutoPlugin, Keys}

/**
  * @author kasonchan
  * @since Jan-2018
  */
object PgbPlugin extends AutoPlugin {

  override lazy val projectSettings = Seq(
    Keys.commands ++= Seq(PgbCommand.download, PgbCommand.unpack)
  )

}

package pgb

import sbt.Command

import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since Jan-2018
  */
object PgbCommand {

  /**
    * Download Gatling bundle.
    * @return zipped Gatling bundle if downloaded successfully.
    */
  def download: Command = Command.args("download", "<version>") {
    (state, args) =>
      val downloadResponse = args match {
        case Seq() =>
          state.log.info(s"Downloading Gatling bundle ${Pgb.gatlingVersion}")
          Pgb.downloadGB()
        case version =>
          state.log.info(s"Downloading Gatling bundle ${version.mkString}")
          Pgb.downloadGB(version.mkString)
      }

      downloadResponse match {
        case Success(s) =>
          s match {
            case 0 =>
              state.log.info(
                s"Downloaded Gatling bundle successfully"
              )
            case _ =>
              state.log.warn(
                s"Failed downloading Gatling bundle and check log for more detail"
              )
          }
        case Failure(e) =>
          state.log.error(
            s"Failed downloading Gatling bundle and check log for more detail ${e.getMessage}"
          )
      }

      state
  }
}

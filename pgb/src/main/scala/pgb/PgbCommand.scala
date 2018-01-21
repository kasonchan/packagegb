package pgb

import pgb.Pgb.{downloadGB, unpackGB}
import sbt.Command

import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since Jan-2018
  */
object PgbCommand {

  def download: Command = Command.args("download", "<version>") {
    (state, args) =>
      val response = args match {
        case Seq()   => downloadGB()
        case version => downloadGB(version.mkString(""))
      }

      response match {
        case Success(s) =>
          s match {
            case 0 => state.log.info(s"Downloaded gatling bundle successfully.")
            case _ => state.log.error(s"Failed downloading gatling bundle.")
          }
        case Failure(e) => state.log.error("Failed downloading gatling bundle.")
      }
      state
  }

  def unpack: Command = Command.args("unpack", "<version>") { (state, args) =>
    val unzipResponse = args match {
      case Seq()   => unpackGB()
      case version => unpackGB(version.mkString(""))
    }

    unzipResponse match {
      case Success(s) =>
        s match {
          case 0 => state.log.info("Unpacked gatling bundle successfully.")
          case _ => state.log.error("Failed unpacking gatling bundle.")
        }
      case Failure(e) => state.log.error("Failed unpacking gatling bundle.")
    }

    state
  }

}

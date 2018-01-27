package pgb

import pgb.Pgb._
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
      val response = args match {
        case Seq()   => downloadGB()
        case version => downloadGB(version.mkString(""))
      }

      response match {
        case Success(s) =>
          s match {
            case 0 => state.log.info(s"Downloaded Gatling bundle successfully.")
            case _ => state.log.error(s"Failed downloading Gatling bundle.")
          }
        case Failure(e) => state.log.error("Failed downloading Gatling bundle.")
      }
      state
  }

  /**
    * Unpack the Gatling bundle.
    * Unzip the downloaded Gatling bundle, copy the essential Gatling bundle
    * files and directories to your project.
    * @return @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def unpack: Command = Command.args("unpack", "<version>") { (state, args) =>
    val unpackResponse = args match {
      case Seq()   => unpackGB()
      case version => unpackGB(version.mkString(""))
    }

    unpackResponse match {
      case Success(s) =>
        s match {
          case 0 => state.log.info("Unpacked Gatling bundle successfully.")
          case _ =>
            state.log.warn(
              "Some files may not be unpacked correctly. Check log for more detail.")
        }
      case Failure(e) =>
        state.log.warn(
          "Some files may not be unpacked correctly. Check log for more detail.")
    }

    state
  }

  /**
    * Pack your build.
    * It removes the Gatling bundle and create a zip file of your whole project
    * at the parent directory.
    * @return @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def pack: Command = Command.args("pack", "") { (state, args) =>
    packGB match {
      case Success(s) =>
        s match {
          case 0 =>
            state.log.info("Packed Gatling bundle and build successfully.")
          case _ => state.log.error("Failed packing Gatling bundle and build.")
        }
      case Failure(e) =>
        state.log.error("Failed packing Gatling bundle and build.")
    }

    state
  }

  /**
    * Clean up code.
    * Default is set to remove everything from Gatling bundle and build files.
    * @return @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def cleanup: Command = Command.args("cleanup", "<option>") { (state, args) =>
    val cleanupResponse = args match {
      case Seq() => cleanupEverything
      case optionSeq =>
        optionSeq.mkString("") match {
          case "-e" => cleanupEverything
          case "-b" => cleanupBuild
          case "-g" => cleanupGB
        }
    }

    cleanupResponse match {
      case Success(s) =>
        s match {
          case 0 => state.log.info("Cleaned up successfully.")
          case _ => state.log.error("Failed cleaning up.")
        }
      case Failure(e) => state.log.error("Failed cleaning up.")
    }

    state
  }

}

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
            case _ =>
              state.log.error(
                s"Failed downloading Gatling bundle. Check log for more detail.")
          }
        case Failure(e) =>
          state.log.error(
            "Failed downloading Gatling bundle. Check log for more detail.")
      }

      state
  }

  /**
    * Unpack the Gatling bundle.
    * Unzip the downloaded Gatling bundle to the parent directory.
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
  def pack: Command = Command.args("pack", "<new gatling bundle name>") {
    (state, args) =>
      args match {
        case Seq() =>
          state.log.error(
            "Failed packing Gatling bundle and build. Check log for more detail.")
        case optionSeq => {
          packGB(newGatlingBundleName = optionSeq.mkString("")) match {
            case Success(s) =>
              s match {
                case 0 =>
                  state.log.info(
                    "Packed Gatling bundle and build successfully.")
                case _ =>
                  state.log.error(
                    "Failed packing Gatling bundle and build. Check log for more detail.")
              }
            case Failure(e) =>
              state.log.error(
                "Failed packing Gatling bundle and build. Check log for more detail.")
          }
        }
      }
      state
  }

  /**
    * Clean up code.
    * Default is set to remove everything from Gatling bundle and build files.
    * @return @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def cleanup: Command = Command.args("cleanup", "<new gatling bundle name>") {
    (state, args) =>
      val cleanupResponse = cleanupGB(newGatlingBundleName = args.mkString(""))

      cleanupResponse match {
        case Success(s) =>
          s match {
            case 0 => state.log.info("Cleaned up successfully.")
            case _ =>
              state.log.error("Failed cleaning up. Check log for more detail.")
          }
        case Failure(e) =>
          state.log.error("Failed cleaning up. Check log for more detail.")
      }

      state
  }

}

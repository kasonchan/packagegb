package pgb

import pgb.Pgb.{dlgb, unzipgb, cpgb}
import sbt.Command

import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since Jan-2018
  */
object PgbCommand {

  def dl: Command = Command.args("dlgb", "<version>") { (state, args) =>
    val response = args match {
      case Seq()   => dlgb()
      case version => dlgb(version.mkString(""))
    }

    response match {
      case Success(s) =>
        s match {
          case 0 => state.log.info("Success")
          case _ => state.log.error("Fail")
        }
      case Failure(e) => state.log.error("Fail")
    }
    state
  }

  def unpack: Command = Command.args("unpack", "<version>") { (state, args) =>
    val unzipResponse = args match {
      case Seq() => unzipgb()
      case version => unzipgb(version.mkString(""))
    }

    unzipResponse match {
      case Success(s) =>
        s match {
          case 0 => state.log.info("Unzip gatling bundle successfully")
          case _ => state.log.error("Failed unzipping gatling bundle")
        }
      case Failure(e) => state.log.error("Failed unzipping gatling bundle")
    }

    val cpResponse = args match {
      case Seq() => cpgb()
      case version => cpgb(version.mkString(""))
    }

    cpResponse match {
      case Success(s) =>
        s match {
          case 0 => state.log.info("Copy gatling bundle successfully")
          case _ => state.log.error("Failed copying gatling bundle")
        }
      case Failure(e) => state.log.error("Failed copying gatling bundle")
    }

    state
  }

  def pack: Command = Command.args("pack", "<version>") { (state, args) =>
    val unzipResponse = args match {
      case Seq() => unzipgb()
      case version => unzipgb(version.mkString(""))
    }

    unzipResponse match {
      case Success(s) =>
        s match {
          case 0 => state.log.info("Unzip gatling bundle successfully")
          case _ => state.log.error("Failed unzipping gatling bundle")
        }
      case Failure(e) => state.log.error("Failed unzipping gatling bundle")
    }

    state
  }

}

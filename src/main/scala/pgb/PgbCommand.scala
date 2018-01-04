package pgb

import pgb.Pgb.{dlgb, gatlingVersion}
import sbt.Command

import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since Jan-2018
  */
object PgbCommand {

  def dl = Command.args("dlgb", "<version>") { (state, args) =>
    val response = args match {
      case Seq()   => dlgb(gatlingVersion)
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

}

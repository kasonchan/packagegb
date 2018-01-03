package kasonchan

import kasonchan.Pgb.dlgb
import sbt.Command

import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since Jan-2018
  */
object PgbCommand {

  def dl = Command.command("dlgb") { state =>
    dlgb match {
      case Success(s) => state.log.info("Success")
      case Failure(e) => state.log.error("Fail")
    }
    state
  }

}

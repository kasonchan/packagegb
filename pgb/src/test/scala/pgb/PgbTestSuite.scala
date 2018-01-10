package pgb

import org.specs2._
import org.specs2.specification.core.{Fragments, SpecStructure}
import org.specs2.specification.script.StandardDelimitedStepParsers
import pgb.Pgb.dlgb

import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since Jan-2018
  */
class PgbTestSuite
    extends Specification
    with specification.dsl.GWT
    with StandardDelimitedStepParsers {
  def is: SpecStructure = s2"""

 When I call dlgb(), I get a zip file and exit code {0} $dlgbSucceed

 When I call dlgb(invalid-version), I get exit code status {22} $dlgbFailed

  """

  def dlgbSucceed: String => Fragments = example(anInt) { expectedExitCode =>
    val exitCode = dlgb() match {
      case Success(s) => s
      case Failure(e) => e
    }

    exitCode must_== expectedExitCode
  }

  def dlgbFailed: String => Fragments = example(anInt) { expectedExitCode =>
    val exitCode = dlgb("invalid-version") match {
      case Success(s) => s
      case Failure(e) => e
    }

    exitCode must_== expectedExitCode
  }

}

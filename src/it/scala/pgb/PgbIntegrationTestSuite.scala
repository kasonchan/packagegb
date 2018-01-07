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
class PgbIntegrationTestSuite
    extends Specification
    with specification.dsl.GWT
    with StandardDelimitedStepParsers {
  def is: SpecStructure = s2"""

 Given I enabled plugin PgbPlugin
   When I enter sbt dlgb
   Then I get the zip file and exit code {0} $dlgbSucceed

   When I type sbt dlgb invalid-version
   Then I get exit code status {22} $dlgbFailed

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
import org.specs2.Specification
import org.specs2.specification.core.{Fragments, SpecStructure}
import org.specs2.specification.dsl.GWT
import org.specs2.specification.script.StandardDelimitedStepParsers

import scala.sys.process._

/**
  * @author kasonchan
  * @since Jan-2018
  */
class PgbIntegrationTestSuite
  extends Specification
    with GWT
    with StandardDelimitedStepParsers {
  def is: SpecStructure = s2"""

 Given I enabled plugin PgbPlugin
   When I enter sbt dlgb
   Then I get the zip file and exit code {0} $dlgbSucceed

   When I type sbt dlgb invalidVersion
   Then I get exit code status {0} but no zip file $dlgbFailed

  """

  def dlgbSucceed: String => Fragments = example(anInt) { expectedExitCode =>
    val exitCode: Int = Process("sbt dlgb").!

    exitCode must_== expectedExitCode
  }

  def dlgbFailed: String => Fragments = example(anInt) { expectedExitCode =>
    val exitCode: Int = Process(List("sbt", "dlgb invalidVersion")).!

    exitCode must_== expectedExitCode
  }

}

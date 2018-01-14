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

  def is: SpecStructure = sequential ^ s2"""

 Given I enabled plugin PgbPlugin
   When I type sbt dlgb invalidVersion
   Then I get exit code status {0} but no zip file {1} and removed {1} $dlgbFailed

   When I enter sbt dlgb
   Then I get exit code {0} and the zip file {0} and removed {0} $dlgbSucceed
  """

  def dlgbFailed: String => Fragments = example(threeInts) { expectedExitCodes =>
    val exitCode: Int = Process(Seq("sbt", "dlgb invalidVersion")).!
    val fileExisted: Int = Process(Seq("find", "gatling-charts-highcharts-bundle-*")).!

    exitCode must_== expectedExitCodes._1
    fileExisted must_== expectedExitCodes._2

    val removedFile: Int = Process(Seq("/bin/sh", "-c", "rm gatling-charts-highcharts-bundle-*")).!
    removedFile must_== expectedExitCodes._3
  }

  def dlgbSucceed: String => Fragments = example(threeInts) { expectedExitCodes =>
    val exitCode: Int = Process("sbt dlgb").!
    val fileExisted: Int = Process(Seq("find", "gatling-charts-highcharts-bundle-*")).!

    exitCode must_== expectedExitCodes._1
    fileExisted must_== expectedExitCodes._2

    val removedFile: Int = Process(Seq("/bin/sh", "-c", "rm gatling-charts-highcharts-bundle-*")).!
    removedFile must_== expectedExitCodes._3
  }

}

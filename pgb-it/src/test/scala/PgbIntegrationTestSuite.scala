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
    When I type sbt download invalidVersion
    Then I get exit code {0}, but no zip file {1} and removed {1} $downloadFailed

    When I enter sbt download
    Then I get exit code {0}, zip files exit code {0} and removed {0} $downloadSucceeded

    When I enter sbt deploy
    Then I get exit code {0}, zip files exit code {0} and removed {0} $deploySucceeded
    
    When I enter sbt deploy 3.3.0
    Then I get exit code {0}, zip files exit code {0} and removed {0} $deployVersionedSucceeded
  """

  def downloadFailed: String => Fragments = example(threeInts) {
    expectedExitCodes =>
      val exitCode: Int =
        Process(Seq("sbt", """download invalidVersion""")).!
      val fileExisted: Int =
        Process(
          Seq("/bin/sh",
              "-c",
              "find ",
              "/tmp -name gatling-charts-highcharts-bundle-*.zip")).!

      exitCode must_== expectedExitCodes._1
      fileExisted must_== expectedExitCodes._2

      val removedFile: Int =
        Process(
          Seq("/bin/sh",
              "-c",
              "rm /tmp/gatling-charts-highcharts-bundle-*.zip")).!
      removedFile must_== expectedExitCodes._3
  }

  def downloadSucceeded: String => Fragments = example(threeInts) {
    expectedExitCodes =>
      val exitCode: Int = Process(Seq("sbt", """download""")).!
      val fileExisted: Int =
        Process(
          Seq("/bin/sh",
              "-c",
              "find /tmp/gatling-charts-highcharts-bundle-*.zip")).!

      exitCode must_== expectedExitCodes._1
      fileExisted must_== expectedExitCodes._2

      val removedFile: Int =
        Process(
          Seq("/bin/sh",
              "-c",
              "rm /tmp/gatling-charts-highcharts-bundle-*.zip")).!
      removedFile must_== expectedExitCodes._3
  }

  def deploySucceeded: String => Fragments = example(threeInts) {
    expectedExitCodes =>
      val exitCode: Int = Process(Seq("sbt", """deploy""")).!
      val fileExisted: Int =
        Process(Seq("/bin/sh", "-c", "find /tmp/pgb-it-0.0.3.zip")).!

      exitCode must_== expectedExitCodes._1
      fileExisted must_== expectedExitCodes._2

      val removedFiles: Int =
        Process(
          Seq(
            "/bin/sh",
            "-c",
            "rm -v ../pgb-it-0.0.3.zip /tmp/pgb-it-0.0.3* /tmp/gatling-charts-highcharts-bundle-*")).!
      removedFiles must_== expectedExitCodes._3

      val removedScripts: Int =
        Process(Seq("/bin/sh", "-c", "rm -rv pgb-scripts")).!
      removedScripts must_== expectedExitCodes._3
  }

  def deployVersionedSucceeded: String => Fragments = example(threeInts) {
    expectedExitCodes =>
      val exitCode: Int = Process(Seq("sbt", """deploy 3.3.0""")).!
      val fileExisted: Int =
        Process(Seq("/bin/sh", "-c", "find /tmp/pgb-it-0.0.3.zip")).!

      exitCode must_== expectedExitCodes._1
      fileExisted must_== expectedExitCodes._2

      val removedFile: Int =
        Process(
          Seq(
            "/bin/sh",
            "-c",
            "rm -v ../pgb-it-0.0.3.zip /tmp/pgb-it-0.0.3* /tmp/gatling-charts-highcharts-bundle-*")).!
      removedFile must_== expectedExitCodes._3

      val removedScripts: Int =
        Process(Seq("/bin/sh", "-c", "rm -rv pgb-scripts")).!
      removedScripts must_== expectedExitCodes._3
  }
}

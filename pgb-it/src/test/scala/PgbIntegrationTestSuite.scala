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
    Then I get exit code status {0} but no zip file {1} and removed {1} $downloadFailed

    When I enter sbt download
    Then I get exit code {0} and the zip file {0} and removed {0} $downloadSucceeded

    When I enter sbt unpack invalidVersion
    Then I get exit code status {0} but no files are copied to current directory $unpackFailed

    Given I have entered sbt download
    When I enter sbt unpack
    Then I get exit code {0} and copied file {0} $unpackSucceeded

    Given I have entered sbt download, sbt unpack
    When I enter sbt pack
    Then I get exit code {0} and zipped project at parent directory. $packSucceeded

    Given I have entered sbt download, sbt unpack, sbt pack
    When I enter sbt cleanup -e
    Then I get exit code {0} and zipped project at parent directory. $cleanupSucceeded
  """

  def downloadFailed: String => Fragments = example(threeInts) {
    expectedExitCodes =>
      val exitCode: Int = Process(Seq("sbt", "download invalidVersion")).!
      val fileExisted: Int =
        Process(
          Seq("/bin/sh",
              "-c",
              "find",
              "gatling-charts-highcharts-bundle-*.zip")).!

      exitCode must_== expectedExitCodes._1
      fileExisted must_== expectedExitCodes._2

      val removedFile: Int = Process(
        Seq("/bin/sh", "-c", "rm gatling-charts-highcharts-bundle-*.zip")).!
      removedFile must_== expectedExitCodes._3
  }

  def downloadSucceeded: String => Fragments = example(threeInts) {
    expectedExitCodes =>
      val exitCode: Int = Process("sbt download").!
      val fileExisted: Int =
        Process(
          Seq("/bin/sh",
              "-c",
              "find ../gatling-charts-highcharts-bundle-*.zip")).!

      exitCode must_== expectedExitCodes._1
      fileExisted must_== expectedExitCodes._2

      val removedFile: Int = Process(
        Seq("/bin/sh", "-c", "rm ../gatling-charts-highcharts-bundle-*.zip")).!
      removedFile must_== expectedExitCodes._3
  }

  def unpackFailed: String => Fragments = example(anInt) { expectedExitCode =>
    val exitCode: Int = Process(Seq("sbt", "unpack invalidVersion")).!

    exitCode must_== expectedExitCode
  }

  def unpackSucceeded: String => Fragments = example(twoInts) {
    expectedExitCodes =>
      val downloadExitCode: Int = Process(Seq("sbt", "download")).!
      val unpackExitCode: Int = Process(Seq("sbt", "unpack")).!

      downloadExitCode must_== expectedExitCodes._1
      unpackExitCode must_== expectedExitCodes._2
  }

  def packSucceeded: String => Fragments = example(anInt) { expectedExitCode =>
    val downloadExitCode: Int = Process(Seq("sbt", "download")).!
    val unpackExitCode: Int = Process(Seq("sbt", "unpack")).!
    val packExitCode: Int = Process(Seq("sbt", "pack test")).!

    downloadExitCode must_== expectedExitCode
    unpackExitCode must_== expectedExitCode
    packExitCode must_== expectedExitCode
  }

  def cleanupSucceeded: String => Fragments = example(anInt) {
    expectedExitCode =>
      val downloadExitCode: Int = Process(Seq("sbt", "download")).!
      val unpackExitCode: Int = Process(Seq("sbt", "unpack")).!
      val packExitCode: Int = Process(Seq("sbt", "pack test")).!
      val cleanupExitCode: Int = Process(Seq("sbt", "cleanup test")).!

      downloadExitCode must_== expectedExitCode
      unpackExitCode must_== expectedExitCode
      packExitCode must_== expectedExitCode
      cleanupExitCode must_== expectedExitCode
  }

}

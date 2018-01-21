package pgb

import org.specs2._
import org.specs2.specification.core.{Fragments, SpecStructure}
import org.specs2.specification.script.StandardDelimitedStepParsers
import pgb.Pgb.{downloadGB, unpackGB, packGB, cleanupEverything}

import scala.sys.process.Process
import scala.util.{Failure, Success}

/**
  * @author kasonchan
  * @since Jan-2018
  */
class PgbTestSuite
    extends Specification
    with specification.dsl.GWT
    with StandardDelimitedStepParsers {
  def is: SpecStructure = sequential ^ s2"""

  When I call downloadGB(), I get a zip file and exit code {0} $downloadGBSucceeded

  When I call downloadGB(invalidVersion), I get exit code status {22} $downloadGBFailed

  When I call unpackGB(), I get unzipped files and exit code {2} $unpackGBSucceeded

  When I call unpackGB(invalidVersion), I get exit code status {12} $unpackGBFailed

  When I call packGB(), I get zipped project and exit code {0} $packGBSucceeded
                                          
  Given I have downloadGB()
    When I call cleanupEverything
    Then I get exit code status {0} $cleanupEverythingSucceeded

  """

  def downloadGBSucceeded: String => Fragments = example(anInt) {
    expectedExitCode =>
      val exitCode = downloadGB() match {
        case Success(s) => s
        case Failure(e) => e
      }

      exitCode must_== expectedExitCode

      val removedFile: Int = Process(
        Seq("/bin/sh", "-c", "rm -rv gatling-charts-highcharts-bundle-*")).!
      removedFile must_== expectedExitCode
  }

  def downloadGBFailed: String => Fragments = example(anInt) {
    expectedExitCode =>
      val exitCode = downloadGB("invalidVersion") match {
        case Success(s) => s
        case Failure(e) => e
      }

      exitCode must_== expectedExitCode
  }

  def unpackGBSucceeded: String => Fragments = example(anInt) {
    expectedExitCode =>
      downloadGB()

      val exitCode = unpackGB() match {
        case Success(s) => s
        case Failure(e) => e
      }

      exitCode must_== expectedExitCode
  }

  def unpackGBFailed: String => Fragments = example(anInt) { expectedExitCode =>
    downloadGB()

    val exitCode = unpackGB("invalidVersion") match {
      case Success(s) => s
      case Failure(e) => e
    }

    exitCode must_== expectedExitCode
  }

  def packGBSucceeded: String => Fragments = example(anInt) {
    expectedExitCode =>
      val exitCode = packGB match {
        case Success(s) => s
        case Failure(e) => e
      }

      exitCode must_== expectedExitCode
  }

  def cleanupEverythingSucceeded = example(anInt) {
    downloadGB()
    unpackGB()
    packGB

    expectedExitCode =>
      val exitCode = cleanupEverything match {
        case Success(s) => s
        case Failure(e) => e
      }

      exitCode must_== expectedExitCode
  }

}

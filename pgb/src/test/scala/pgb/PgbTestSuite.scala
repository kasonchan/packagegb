package pgb

import org.specs2._
import org.specs2.specification.core.{Fragments, SpecStructure}
import org.specs2.specification.script.StandardDelimitedStepParsers
import pgb.Pgb.{dlgb, unzipgb}

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

 When I call dlgb(), I get a zip file and exit code {0} $dlgbSucceed

 When I call dlgb(invalidVersion), I get exit code status {22} $dlgbFailed

 When I call unzipgb(), I get a zip file and exit code {0} $unzipgbSucceed

 When I call unzipgb(invalidVersion), I get exit code status {9} $unzipgbFailed

  """

  def dlgbSucceed: String => Fragments = example(anInt) { expectedExitCode =>
    val exitCode = dlgb() match {
      case Success(s) => s
      case Failure(e) => e
    }

    exitCode must_== expectedExitCode

    val removedFile: Int = Process(Seq("/bin/sh", "-c", "rm -r gatling-charts-highcharts-bundle-*")).!
    removedFile must_== expectedExitCode
  }

  def dlgbFailed: String => Fragments = example(anInt) { expectedExitCode =>
    val exitCode = dlgb("invalidVersion") match {
      case Success(s) => s
      case Failure(e) => e
    }

    exitCode must_== expectedExitCode
  }

  def unzipgbSucceed: String => Fragments = example(anInt) { expectedExitCode =>
    dlgb()

    val exitCode = unzipgb() match {
      case Success(s) => s
      case Failure(e) => e
    }

    exitCode must_== expectedExitCode

    val removedFile: Int = Process(Seq("/bin/sh", "-c", "rm -r gatling-charts-highcharts-bundle-*")).!
    removedFile must_== expectedExitCode
  }

  def unzipgbFailed: String => Fragments = example(anInt) { expectedExitCode =>
    val exitCode = unzipgb("invalidVersion") match {
      case Success(s) => s
      case Failure(e) => e
    }

    exitCode must_== expectedExitCode
  }

}

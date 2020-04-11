package pgb

import org.scalatest.TryValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.{Success, Try}
import scala.sys.process._

/**
  * @author kasonchan
  * @since Jan-2018
  */
class PgbTestSuite extends AnyWordSpec with Matchers with TryValues {
  private val expectedSuccessDownloadExitCode = 0
  private val expectedFailedDownloadExitCode = 22

  "pgb" must {
    "be able to download a current release version of gatling bundle when downloadGB() is called" in {
      Pgb.downloadGB() mustBe Try(expectedSuccessDownloadExitCode)
      Process(Seq("/bin/sh",
                  "-c",
                  "rm -rv /tmp/gatling-charts-highcharts-bundle-*")).!
    }

    "be able to not to download a invalid version of gatling bundle when downloadGB() is called" in {
      Pgb.downloadGB("invalidVersion").success mustBe Success(
        expectedFailedDownloadExitCode)
      Process(Seq("/bin/sh",
                  "-c",
                  "rm -rv /tmp/gatling-charts-highcharts-bundle-*")).!
    }

    "be able to deploy your project bundled with gatling when deployGB() is called" in {
      Pgb.deployGB().isSuccess mustBe true
      Process(Seq("/bin/sh", "-c", "rm -rv pgb-scripts*")).!
    }
  }
}

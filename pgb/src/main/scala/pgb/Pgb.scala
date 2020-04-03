package pgb

import scala.sys.process._
import scala.util.Try

/**
  * @author kasonchan
  * @since Jan-2018
  */
object Pgb {

  /**
    * Current Gatling release version
    */
  lazy val gatlingVersion = "3.3.1"

  /**
    * Execute curl to download gatling bundle to the parent directory.
    * @param version String User specified version.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def downloadGB(version: String = gatlingVersion): Try[Int] = Try {
    lazy val link = s"https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/" +
      s"$version/gatling-charts-highcharts-bundle-$version-bundle.zip"
    s"curl -f0 $link --output ../gatling-charts-highcharts-bundle-$version.zip".!
  }

  /**
    * Execute unzip gatling bundle files and directories to the parent directory.
    * @param version String User specified version.
    * @return Try(0) if process is executed successfully. Otherwise return
    *         Try(nonzero).
    */
  def unpackGB(version: String = gatlingVersion): Try[Int] = {
    for {
      unzipGBResponse <- unzipGB(version)
    } yield unzipGBResponse
  }

  /**
    * Execute unzip the downloaded gatling bundle.
    * @param version String User specified version.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  private def unzipGB(version: String = gatlingVersion): Try[Int] = Try {
    s"unzip ../gatling-charts-highcharts-bundle-$version.zip -d ..".!
  }

  /**
    * Execute copy simulation files and directories to the corresponding directories.
    * @param version String User specified version.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  private def cpSims(version: String = gatlingVersion,
                     newGatlingBundleName: String): Try[Int] = {
    lazy val dataExitCode = Try {
      Seq(
        "/bin/sh",
        "-c",
        s"\\cp -fpRv src/test/resources/data/* ../${newGatlingBundleName}/user-files/data").!
    }

    lazy val resourcesExitCode = Try {
      Seq(
        "/bin/sh",
        "-c",
        s"\\cp -fpRv src/test/resources/bodies/* ../${newGatlingBundleName}/user-files/bodies").!
    }

    lazy val cleanupSims = Try {
      Seq("/bin/sh",
          "-c",
          s"rm -rfv ../${newGatlingBundleName}/user-files/simulations/*").!
    }

    lazy val scalaExitCode = Try {
      Seq(
        "/bin/sh",
        "-c",
        s"\\cp -fpRv src/test/scala/* ../${newGatlingBundleName}/user-files/simulations").!
    }

    for {
      d <- dataExitCode
      r <- resourcesExitCode
      c <- cleanupSims
      s <- scalaExitCode
    } yield d + r + c + s
  }

  /**
    * Execute remove just gatling bundle files and directories and zip the project.
    * @param version String User specified version.
    * @param newGatlingBundleName String User specified new bundle name.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def packGB(version: String = gatlingVersion,
             newGatlingBundleName: String): Try[Int] = {
    lazy val zipExitCode = Try {
      s"zip -rv ../$newGatlingBundleName.zip ../$newGatlingBundleName".!
    }

    for {
      m <- mvGB(version, newGatlingBundleName)
      c <- cpSims(version, newGatlingBundleName)
      z <- zipExitCode
    } yield m + c + z
  }

  /**
    * Execute rename the unzipped update directory with newGatlingBundleName.
    * @param version String User specified version.
    * @param newGatlingBundleName String User specified new bundle name.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  private def mvGB(version: String = gatlingVersion,
                   newGatlingBundleName: String): Try[Int] = Try {
    Seq(
      "/bin/sh",
      "-c",
      s"mv -v ../gatling-charts-highcharts-bundle-$version ../$newGatlingBundleName").!
  }

  /**
    * Execute remove Gatling bundle directory and packed directory.
    * @param newGatlingBundleName String User specified new bundle name.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def cleanupGB(newGatlingBundleName: String): Try[Int] =
    Try {
      Seq(
        "/bin/sh",
        "-c",
        s"rm -frv ../gatling-charts-highcharts-bundle-* ../$newGatlingBundleName").!
    }
}

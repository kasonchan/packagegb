package pgb

import scala.sys.process._
import scala.util.Try

/**
  * @author kasonchan
  * @since Jan-2018
  */
object Pgb {

  lazy val gatlingVersion = "2.3.0"

  /**
    * Execute curl -fO to download gatling bundle.
    * @param version User specified version.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def downloadGB(version: String = gatlingVersion): Try[Int] = Try {
    lazy val link = s"https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/" +
      s"$version/gatling-charts-highcharts-bundle-$version-bundle.zip"
    s"curl -fO $link".!
  }

  /**
    * Execute unzip gatling bundle files and directories to your project.
    * Copy the essential gatling bundle files and directories to your
    * corresponding directories of your project.
    * @param version User specified version.
    * @return Try(0) if process is executed successfully. Otherwise return
    *         Try(nonzero).
    */
  def unpackGB(version: String = gatlingVersion): Try[Int] = {
    for {
      unzipGBResponse <- unzipGB(version)
      cpGBResponse <- cpGB(version)
      cpSimsResponse <- cpSims
    } yield unzipGBResponse + cpGBResponse + cpSimsResponse
  }

  /**
    * Execute Unzip the downloaded gatling bundle.
    * @param version User specified version.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def unzipGB(version: String = gatlingVersion): Try[Int] = Try {
    s"unzip gatling-charts-highcharts-bundle-$version-bundle.zip".!
  }

  /**
    * Execute Copy unzipped Gatling bundle to current directory.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def cpGB(version: String = gatlingVersion): Try[Int] = {
    lazy val moveExitCode = Try {
      Seq("/bin/sh",
          "-c",
          s"\\cp -fpRv gatling-charts-highcharts-bundle-$version/* .").!
    }

    lazy val removeExitCode = Try {
      Seq("/bin/sh", "-c", "rm -rv user-files/simulations/*").!
    }

    for {
      m <- moveExitCode
      r <- removeExitCode
    } yield m + r
  }

  /**
    * Execute copy simulation files and directories to the corresponding directories.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def cpSims: Try[Int] = {
    lazy val dataExitCode = Try {
      Seq("/bin/sh",
          "-c",
          "\\cp -fpRv src/test/resources/data/* user-files/data").!
    }

    lazy val resourcesExitCode = Try {
      Seq("/bin/sh",
          "-c",
          "\\cp -fpRv src/test/resources/bodies/* user-files/bodies").!
    }

    lazy val scalaExitCode = Try {
      Seq("/bin/sh", "-c", "\\cp -fpRv src/test/scala/* user-files/simulations").!
    }

    for {
      d <- dataExitCode
      r <- resourcesExitCode
      s <- scalaExitCode
    } yield d + r + s
  }

  /**
    * Execute remove just gatling bundle files and directories and zip the project.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def packGB: Try[Int] = {
    val currentPath = System.getProperty("user.dir")
    lazy val basename: String = s"basename $currentPath".!!
    lazy val zipExitCode = Try {
      s"zip -rv ../$basename.zip ." !
    }

    for {
      r <- cleanupGB
      z <- zipExitCode
    } yield r + z
  }

  /**
    * Execute remove all Gatling bundle files and directories, build and zipped build.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def cleanupEverything: Try[Int] = {
    val gbDirectories = List(
      "bin",
      "conf",
      "lib",
      "results",
      "user-files",
      "LICENSE",
      "gatling-charts-highcharts-bundle-*",
      s"../${new java.io.File(".").getCanonicalFile.getName}.zip")

    Try {
      Seq("/bin/sh", "-c", s"rm -rv ${gbDirectories.mkString(" ")}").!
    }
  }

  /**
    * Execute remove just Gatling bundle directory and zipped file.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def cleanupGB: Try[Int] = Try {
    Seq("/bin/sh", "-c", "rm -frv gatling-charts-highcharts-bundle-*").!
  }

  /**
    * Execute remove the zipped build.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def cleanupBuild: Try[Int] = Try {
    s"rm -rv ../${new java.io.File(".").getCanonicalFile.getName}.zip".!
  }

}

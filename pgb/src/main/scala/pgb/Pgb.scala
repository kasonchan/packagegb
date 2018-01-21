package pgb

import scala.sys.process._
import scala.util.{Success, Try}

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
    * Copy the gatling bundle files and directories to corresponding directories
    * of your project.
    * Clean unzipped gatling bundle files and directories.
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
    * Unzip the downloaded gatling bundle.
    * @param version User specified version.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def unzipGB(version: String = gatlingVersion): Try[Int] = Try {
    s"unzip gatling-charts-highcharts-bundle-$version-bundle.zip".!
  }

  /**
    * Copy unzipped Gatling bundle to current directory.
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
    * Copy simulation files and directories to the corresponding directories.
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
    * Remove all Gatling bundle files and directories.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def cleanGB: Try[Int] = {
    val gbDirectories = List("bin",
                             "conf",
                             "lib",
                             "results",
                             "user-files",
                             "LICENSE",
                             "gatling-charts-highcharts-bundle-*")

    Try {
      Seq("/bin/sh", "-c", s"rm -rv ${gbDirectories.mkString(" ")}").!
    }
  }

  /**
    * Remove all build file and Gatling bundle files and directories.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  private def cleanBuild: Try[Int] = {
    lazy val rmBuild = Try {
      s"rm -rv ../${new java.io.File(".").getCanonicalFile.getName}.zip".!
    }

    for {
      r <- rmBuild
    } yield r
  }

}

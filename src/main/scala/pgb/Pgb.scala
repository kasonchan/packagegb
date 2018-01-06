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
    * Set up download Gatling bundle
    */
  def dlgb(version: String): Try[Int] = Try {
    lazy val link = s"https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/" +
      s"$version/gatling-charts-highcharts-bundle-$version-bundle.zip"
    s"curl -fO $link".!
  }

}

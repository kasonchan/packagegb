package pgb

import java.io.{File, PrintWriter}

import scala.sys.process._
import scala.util.Try

/**
  * @author kasonchan
  * @since Jan-2018
  */
object Pgb {
  val gatlingVersion: String = "3.3.1"
  val scriptPath = "pgb-scripts"
  val scriptName = s"$scriptPath/pgb-deploy.sh"

  /**
    * Execute curl to download gatling bundle to the parent directory.
    * @param version String User specified version.
    * @return Try(0) if process is executed successfully. Otherwise return Try(nonzero).
    */
  def downloadGB(version: String = gatlingVersion): Try[Int] = Try {
    val link = s"https://repo1.maven.org/maven2/io/gatling/highcharts/" +
      s"gatling-charts-highcharts-bundle/$version/gatling-charts-highcharts-bundle-$version-bundle.zip"
    s"curl -f0 $link --output /tmp/gatling-charts-highcharts-bundle-$version.zip".!
  }

  private val scriptText: String =
    """#!/bin/bash

#
# @author kasonchan
# @since 2020-04
#

CURRENT_DIRECTORY=$PWD
PARENT_DIRECTORY=${PWD%/*}
BASENAME=$(basename "$CURRENT_DIRECTORY")

if [ "$BASENAME" == "scripts" ]; then
  echo "[error] Please run deploy.sh at your project directory. Usage: ./pgb-scripts/pgb-deploy.sh <package_name> <gatling_bundle_version>"
  exit 1
else
  PROJECT_NAME=$1
  GATLING_BUNDLE_VERSION=$2

  if [ $# -ne 2 ]; then
    echo "Usage: ./pgb-scripts/pgb-deploy.sh <package_name> <gatling_bundle_version>"
  else
    echo "$CURRENT_DIRECTORY"
    echo "$PARENT_DIRECTORY"

    # Download Gatling bundle with user specified version to /tmp
    LINK="https://repo1.maven.org/maven2/io/gatling/highcharts/"
    LINK+="gatling-charts-highcharts-bundle/$GATLING_BUNDLE_VERSION/"
    LINK+="gatling-charts-highcharts-bundle-$GATLING_BUNDLE_VERSION-bundle.zip"
    curl -f0 $LINK --output /tmp/gatling-charts-highcharts-bundle-$GATLING_BUNDLE_VERSION.zip

    if [ $? -ne 0 ]; then
      echo "[error] Invalid Gatling bundle version $GATLING_BUNDLE_VERSION"
    else
      echo "[info] Downloaded Gatling bundle $GATLING_BUNDLE_VERSION successfully"

      # Unzip Gatling bundle and rename it to package name in /tmp
      unzip /tmp/gatling-charts-highcharts-bundle-"$GATLING_BUNDLE_VERSION".zip -d /tmp
      mv -v /tmp/gatling-charts-highcharts-bundle-"$GATLING_BUNDLE_VERSION" /tmp/"$PROJECT_NAME"
      echo "[info] Unzipped Gatling bundle successfully"

      rm -rfv /tmp/"$PROJECT_NAME"/LICENSE
      rm -rfv /tmp/"$PROJECT_NAME"/user-files/simulations/*
      rm -rfv /tmp/"$PROJECT_NAME"/user-files/resources/*
      rm -rfv /tmp/"$PROJECT_NAME"/results

      # Copy project to Gatling bundle
      cp -fpRv src/test/scala/* /tmp/$PROJECT_NAME/user-files/simulations
      cp -fpRv src/test/resources/* /tmp/$PROJECT_NAME/user-files/resources

      # Copy resources *.conf and *.xml to Gatling bundle conf
      cp -fpRv /tmp/"$PROJECT_NAME"/user-files/resources/*.conf /tmp/"$PROJECT_NAME"/conf
      cp -fpRv /tmp/"$PROJECT_NAME"/user-files/resources/*.xml /tmp/"$PROJECT_NAME"/conf
      rm -rfv /tmp/"$PROJECT_NAME"/user-files/resources/*.conf
      rm -rfv /tmp/"$PROJECT_NAME"/user-files/resources/*.xml
      echo "[info] Copied your project to Gatling bundle successfully"

      # Zip the package
      cd /tmp
      zip -rv "$PROJECT_NAME".zip "$PROJECT_NAME" -x "*.DS_Store"
      echo "[info] Zipped your project successfully"

      # Copy zipped project to parent directory
      cp -fpRv /tmp/"$PROJECT_NAME".zip "$PARENT_DIRECTORY"
      echo "[info] Copied your zipped project to parent directory"
    fi
  fi
fi
""".stripMargin

  /**
    * Execute creating deploy script.
    * @return Try(Unit) if process is executed.
    */
  def deployGB(): Try[Unit] = Try {
    val newDirectory = new File(scriptPath)
    newDirectory.mkdir()

    val script = new File(scriptName)
    val writer = new PrintWriter(script)
    writer.write(scriptText)
    writer.close()

    script.setExecutable(true)
  }
}

#!/bin/bash

#
# @author kasonchan
# @since 2020-04
#

CURRENT_DIRECTORY=$PWD
PARENT_DIRECTORY=${PWD%/*}
BASENAME=$(basename "$CURRENT_DIRECTORY")

if [ "$BASENAME" == "scripts" ]; then
  echo "[error] Please run deploy.sh at your project directory. Usage: ./scripts/deploy.sh <package_name> <gatling_bundle_version>"
  exit 1
else
  PACKAGE_NAME=$1
  GATLING_BUNDLE_VERSION=$2

  if [ $# -ne 2 ]; then
    echo "Usage: ./script/deploy.sh <package_name> <gatling_bundle_version>"
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
      echo "[info] Downloaded Gatling bundle $GATLING_BUNDLE_VERSION successfully

      # Unzip Gatling bundle and rename it to package name in /tmp
      unzip /tmp/gatling-charts-highcharts-bundle-"$GATLING_BUNDLE_VERSION".zip -d /tmp
      mv -v /tmp/gatling-charts-highcharts-bundle-"$GATLING_BUNDLE_VERSION" /tmp/"$PACKAGE_NAME"
      echo "[info] Unzipped Gatling bundle successfully"

      rm -rfv /tmp/"$PACKAGE_NAME"/LICENSE
      rm -rfv /tmp/"$PACKAGE_NAME"/user-files/simulations/*
      rm -rfv /tmp/"$PACKAGE_NAME"/user-files/resources/*
      rm -rfv /tmp/"$PACKAGE_NAME"/results

      # Copy project to Gatling bundle
      cp -fpRv src/test/scala/* /tmp/$PACKAGE_NAME/user-files/simulations
      cp -fpRv src/test/resources/* /tmp/$PACKAGE_NAME/user-files/resources

      # Copy resources *.conf and *.xml to Gatling bundle conf
      cp -fpRv /tmp/"$PACKAGE_NAME"/user-files/resources/*.conf /tmp/"$PACKAGE_NAME"/conf
      cp -fpRv /tmp/"$PACKAGE_NAME"/user-files/resources/*.xml /tmp/"$PACKAGE_NAME"/conf
      rm -rfv /tmp/"$PACKAGE_NAME"/user-files/resources/*.conf
      rm -rfv /tmp/"$PACKAGE_NAME"/user-files/resources/*.xml
      echo "[info] Copied your project to Gatling bundle successfully"

      # Zip the package
      cd /tmp
      zip -rv "$PACKAGE_NAME".zip "$PACKAGE_NAME" -x "*.DS_Store"
      echo "[info] Zipped your project successfully"

      # Copy zipped project to parent directory
      cp -fpRv /tmp/"$PACKAGE_NAME".zip "$PARENT_DIRECTORY"
      echo "[info] Copied your zipped project to parent directory"
    fi
  fi
fi

# pgb

[![Build Status](https://travis-ci.org/kasonchan/pgb.svg?branch=master)](https://travis-ci.org/kasonchan/pgb)

A simple sbt plugin for automating package and deploy your test project with Gatling bundle.

## Installation

Add the following to your `project/plugins.sbt` file:

```
addSbtPlugin("com.kasonchan" % "pgb" % "0.0.3")
```

In your `build.sbt`, enable the plugin by adding the following, and 
specify the Gatling version `gatlingVersion := "<version>"` that you want 
your project to bundle with:

```
enablePlugins(PgbPlugin)

gatlingVersion := "3.3.1"
```

Current Gatling bundle release verion is `3.3.1`.

## SBT Commands

- Deploy your test project

  - pgb downloads and creates zipped test project with Gatling bundle verison 
  `gatlingVersion` that you specified in `build.sbt`.


```
sbt> deploy
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100 60.1M  100 60.1M    0     0  1168k      0  0:00:52  0:00:52 --:--:-- 1076k
...
[info] Project <name>-<version>
[info] Gatling bundle <gatlingVersion>
[info] Downloaded Gatling bundle <gatlingVersion> successfully
[info] Unzipped Gatling bundle successfully
[info] Copied your project to Gatling bundle successfully
[info] Zipped your project successfully
[info] Copied your zipped project to parent directory
```

- Download Gatling bundle

  - pgb downloads current Gatling bundle. 
  You can optionally specify a Gatling bundle version to be downloaded.

```
sbt> download
[info] Downloading Gatling bundle 3.3.1
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100 60.1M  100 60.1M    0     0  1168k      0  0:00:52  0:00:52 --:--:-- 1076k
[info] Downloaded Gatling bundle successfully

sbt> download <version>
[info] Downloading Gatling bundle <version>
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100 60.1M  100 60.1M    0     0  1168k      0  0:00:52  0:00:52 --:--:-- 1076k
[info] Downloaded Gatling bundle successfully
```

## Gotchas

If your test project includes other library dependencies that are not included 
in Gatling bundle, you will need to include them by yourself.

## License

This code is open source software licensed under the [MIT](https://opensource.org/licenses/MIT) license.

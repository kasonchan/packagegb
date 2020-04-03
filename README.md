# pgb

[![Build Status](https://travis-ci.org/kasonchan/pgb.svg?branch=master)](https://travis-ci.org/kasonchan/pgb)

A simple sbt plugin for automating package and deploy Gatling bundle and test.

## Installation

Add the following to your `project/plugins.sbt` file:

```
addSbtPlugin("com.kasonchan" % "pgb" % "0.0.2")
```

In your `build.sbt` enable the plugin by adding the following:

```
.enablePlugins(PgbPlugin)
```

## SBT Commands

- Download Gatling bundle:

```
sbt:pgb-it> download
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100 55.3M  100 55.3M    0     0   329k      0  0:02:51  0:02:51 --:--:-- 1356k
[info] Downloaded Gatling bundle successfully.
```

- Unpack Gatling bundle:

```
sbt:pgb-it> unpack
```

- Pack Gatling bundle and your project:

```
sbt:pgb-it> pack
```

- Cleanup Gatling bundle:

```
sbt:pgb-it> cleanup
```

## License

This code is open source software licensed under the [MIT](https://opensource.org/licenses/MIT) license.

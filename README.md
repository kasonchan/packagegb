# pgb

[![Build Status](https://travis-ci.org/kasonchan/pgb.svg?branch=master)](https://travis-ci.org/kasonchan/pgb)

A simple sbt plugin for automating package and deploy Gatling bundle and test.

## Installation

Add the following to your `project/plugins.sbt` file:

```
addSbtPlugin("com.kasonchan" % "pgb" % "0.0.1")
```

In your `build.sbt` enable the plugin by adding the following:

```
.enablePlugins(PgbPlugin)
```

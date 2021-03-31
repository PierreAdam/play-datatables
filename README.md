# Play-DataTables

[![Latest release](https://img.shields.io/github/v/release/PierreAdam/play-datatables)](https://github.com/PierreAdam/play-datatables/releases/latest)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/8078f113e10049f1abdb621da80c8928)](https://www.codacy.com/gh/PierreAdam/play-datatables/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=PierreAdam/play-datatables&amp;utm_campaign=Badge_Grade)
[![Code Climate maintainability](https://img.shields.io/codeclimate/maintainability-percentage/PierreAdam/play-datatables)](https://codeclimate.com/github/PierreAdam/play-datatables)
[![Codecov](https://codecov.io/gh/PierreAdam/play-datatables/branch/master/graph/badge.svg)](https://codecov.io/gh/PierreAdam/play-datatables)
[![Snyk Vulnerabilities](https://img.shields.io/snyk/vulnerabilities/github/PierreAdam/play-datatables)](https://snyk.io/test/github/PierreAdam/play-datatables?targetFile=pom.xml)
![JDK](https://img.shields.io/badge/JDK-1.8+-blue.svg)
[![Build Status](https://travis-ci.com/PierreAdam/play-datatables.svg?branch=master)](https://travis-ci.com/PierreAdam/play-datatables)
[![GitHub license](https://img.shields.io/github/license/PierreAdam/play-datatables)](https://raw.githubusercontent.com/PierreAdam/play-ebean-datatables/master/LICENSE)

Play-DataTables is a library for play framework that allows you to easily
integrate [DataTables](https://datatables.net/) in your PlayFramework project.

This library is providing an abstraction for the DataTables requests. __It is not recommended to directly utilize this
library__ as it's the base for other libraries that implements some data providers.

List of the current data providers :

- [play-ebean-datatables](https://github.com/PierreAdam/play-ebean-datatables) Directly query your database from
  DataTables
- [play-httpquery-datatables](https://github.com/PierreAdam/play-httpquery-datatables) Allow you to use an API as a
  source for DataTables

*****

## Build the library and local deployment

```shell
$> mvn compile
$> mvn package
$> mvn install
```

### Install or deploy

To install in your local repository.

```shell
$> mvn install
```

To deploy to a remote repository.

```shell
$> mvn verify
$> mvn deploy -P release
```

## How to import the library

### With Sbt

```scala
libraryDependencies += "com.jackson42.play" % "play-datatables" % "21.04u1"
```

### With Maven

```xml

<dependency>
    <groupId>com.jackson42.play</groupId>
    <artifactId>play-datatables</artifactId>
    <version>21.04u1</version>
</dependency>
``` 

### Implementing a data provider for Play-DataTables

Example of implementation is available in the tests. For a more concrete example, take a look
at [play-ebean-datatables](https://github.com/PierreAdam/play-ebean-datatables)

## Versions

| Library Version | Play Version | Tested DataTables Version  |
|-----------------|--------------|----------------------------|
| 21.04u1         | 2.8.x        | 1.10.x                     |
| 21.04           | 2.8.x        | 1.10.x                     |
| 21.03           | 2.8.x        | 1.10.x                     |

### Changelog

#### 21.04u1

- Patch NPE when a value is at null in the entity.

#### 21.04

- Add a new concept of converters that allow to have a custom object being displayed properly by Play DataTables.
- Refactoring of some complicated part of the code.
- Compilation is now made to be done with Java 11 but compiled for Java 1.8.

## License

This project is released under terms of
the [MIT license](https://raw.githubusercontent.com/PierreAdam/play-ebean-datatables/master/LICENSE).

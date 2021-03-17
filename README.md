# Play-DataTables

[![Latest release](https://img.shields.io/github/v/release/PierreAdam/play-datatables)](https://github.com/PierreAdam/play-datatables/releases/latest)
[![Build Status](https://travis-ci.com/PierreAdam/play-datatables.svg?branch=master)](https://travis-ci.com/PierreAdam/play-datatables)
[![GitHub license](https://img.shields.io/github/license/PierreAdam/play-datatables)](https://raw.githubusercontent.com/PierreAdam/play-ebean-datatables/master/LICENSE)

Play-DataTables is a library for play framework that allows you to easily integrate [DataTables](https://datatables.net/) in your PlayFramework project.

This library is providing an abstraction for the DataTables requests. __It is not recommended to directly utilize this library__ as it's the base for other libraries that
implements some data providers.

List of the current data providers :

- [play-ebean-datatables](https://github.com/PierreAdam/play-ebean-datatables) Directly query your database from DataTables
- [play-httpquery-datatables](https://github.com/PierreAdam/play-httpquery-datatables) Allow you to use an API as a source for DataTables

*****

## Build the library and local deployment

```shell
$> mvn compile
$> mvn package
$> mvn install
```

### Deployment

```shell
$> mvn verify
$> mvn deploy
```

## How to import the library

### With Sbt

```scala
libraryDependencies += "com.jackson42.play" % "play-datatables" % "21.03"
```

### With Maven

```xml
<dependency>
    <groupId>com.jackson42.play</groupId>
    <artifactId>play-datatables</artifactId>
    <version>21.03</version>
</dependency>
``` 

### Implementing a data provider for Play-DataTables

Example of implementation is available in the tests. For a more concrete example, take a look at [play-ebean-datatables](https://github.com/PierreAdam/play-ebean-datatables)

## Versions

| Library Version | Play Version | Tested DataTables Version  |
|-----------------|--------------|----------------------------|
| 21.03           | 2.8.x        | 1.10.x                     |

## License

This project is released under terms of the [MIT license](https://raw.githubusercontent.com/PierreAdam/play-ebean-datatables/master/LICENSE).

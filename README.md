## NextBus Assignment

### Table of Contents
- [Overview](#overview)
- [Building](#building)
- [Running](#running)
- [System Test](#system_test)
- [TODO](#todo)

***
<div id="overview"/>

### Overview

In this programming assignment, I implement a client that consumes the REST-based API at http://svc.metrotransit.org to provide the arrival of the next bus given a route, direction, and stop.

***
<div id="building"/>

### Building

In order to build, you need the following:

* A Java 8 JDK (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* The Apache Maven build tool (https://maven.apache.org)
* A sane UNIX shell (bash or zsh maybe bash on cygwin for Windows)

This project was built using the following versions of the JDK:

    % java -version
    java version "1.8.0_161"
    Java(TM) SE Runtime Environment (build 1.8.0_161-b12)
    Java HotSpot(TM) 64-Bit Server VM (build 25.161-b12, mixed mode)

and Maven:

    % mvn -version
    Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-10T10:41:47-06:00)
    Maven home: /Users/cching/apps/maven/latest
    Java version: 1.8.0_161, vendor: Oracle Corporation
    Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0_161.jdk/Contents/Home/jre
    Default locale: en_US, platform encoding: UTF-8
    OS name: "mac os x", version: "10.12.6", arch: "x86_64", family: "mac"

and zsh:

    % zsh --version
    zsh 5.2 (x86_64-apple-darwin16.0)

Once the JDK and Maven are correctly installed, the project can be built by executing (from the top-level directory):

    % mvn clean install

This will build the project and run unit tests.

***
<div id="running"/>

### Running

To run the command line program, make sure the project has built successfully.  Then, you can run the `runCommandline.sh` shell script from the root directory.  The command-line utility supports two modes of operation.  The first is as specified in the assignment:

    % nextbus <route> <stop> <direction>

The other way to run it is with robust command-line options.  This allows the user to get a list of routes, directions, and stops making it easier to debug and track down problems:

    You may specify <route> <stop> <direction> or you may use the robust options below.

    Option                Description
    ------                -----------
    -?, -h, --help        show help
    --direction <String>  A valid direction on the given route, one of south, east,
                            west, or north
    --list                List routes, directions, or stops depending on given
                            arguments
    --route <String>      A valid route
    --stop <String>       A valid stop on the given route

***
<div id="system_test"/>

### System Test

There is an exhaustive system test that is disabled in the project.  To run it ...

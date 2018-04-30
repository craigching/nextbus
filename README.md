## NextBus Assignment

### Table of Contents
- [Overview](#overview)
- [Note to reveiwers](#reviewers)
- [Building](#building)
- [Running](#running)
- [System Test](#system_test)
- [TODO](#todo)

***
<div id="overview"/>

### Overview

In this programming assignment, I implement a client that consumes the REST-based API at http://svc.metrotransit.org to provide the arrival of the next bus given a route, direction, and stop.  There are two versions of the client, a command line utility and a web user interface.

***
<div id="reviewers"/>

### Note to reviewers

This repository contains multiple projects.  `nextbus-core` is the core of the project and provides a command line interface for requesting the next departure from a bus stop.  The command line utility is documented below.  `nextbus-core` is the most robustly implemented project.  There are numerous unit tests as well as an exhaustive system test to prove the functionality of `nextbus-core`.  The focus of review should be on the code implemented here.

There are also two stretch goals implemented in the projects `nextbus-service` and `nextbus-docker`.  These projects are less robust as they were more ambitious with less time given for implementation and testing.  Nevertheless, they are built on `nextbus-core` and provide fairly robust functionality.  There is a hosted instance of these two projects on Amazon Web Services [here](http://ec2-34-215-85-179.us-west-2.compute.amazonaws.com:8080/).  Please note that the UI is *very* minimal, but functional.  I take full responsibility for any shortcomings associated with any of these projects!

***
<div id="building"/>

### Building

In order to build, you need the following:

* A Java 8 JDK (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* The Apache Maven build tool (https://maven.apache.org)
* A sane UNIX shell (bash or zsh maybe bash on cygwin for Windows)
* Optionally, you need docker to build `nextbus-docker`

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

and Docker CE:

    % docker version
    Client:
     Version:      18.03.1-ce
     API version:  1.37
     Go version:   go1.9.5
     Git commit:   9ee9f40
     Built:        Thu Apr 26 07:17:20 2018
     OS/Arch:      linux/amd64
     Experimental: false
     Orchestrator: swarm

    Server:
     Engine:
      Version:      18.03.1-ce
      API version:  1.37 (minimum version 1.12)
      Go version:   go1.9.5
      Git commit:   9ee9f40
      Built:        Thu Apr 26 07:15:30 2018
      OS/Arch:      linux/amd64
      Experimental: false

Once the JDK and Maven are correctly installed, the project can be built by executing (from the top-level directory):

    % mvn clean install

This will build the project and run unit tests.  If you don't want to install docker, you can build with the following command:

    % mvn -pl "\!nextbus-docker" clean install

***
<div id="running"/>

### Running

To run the command line program, make sure the project has built successfully.  Then, you can run the `nextbus.sh` shell script from the root directory.  The command-line utility supports two modes of operation.  The first is as specified in the assignment:

    % ./nextbus.sh <route> <stop> <direction>

For example:

    % ./nextbus.sh "METRO Blue Line" "Target Field Station Platform 1" south
    5 minutes

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

For example:

    % ./nextbus.sh --route "METRO Blue Line" --stop "Target Field Station Platform 1" --direction south
    5 minutes

If you want to run the java dropwizard service, you can run it using the `service.sh` script from the root directory:

    % ./service.sh

Assuming you don't have port conflicts with 8080 and 8081, you should then be able to access the UI from [localhost:8080](http://localhost:8080).  If you built the docker service, you can start it with:

    % ./docker-run.sh

This will start a docker container and the UI should be accessible from [localhost:8080](http://localhost:8080).

***
<div id="system_test"/>

### System Test

There is an exhaustive system test that is disabled in the project.  To run it, uncomment the two lines [here](https://github.com/craigching/nextbus/blob/master/nextbus-core/src/test/java/net/webasap/nextbus/core/TestSystemExhaustive.java#L91) and [here](https://github.com/craigching/nextbus/blob/master/nextbus-core/src/test/java/net/webasap/nextbus/core/TestSystemExhaustive.java#L129) then run `mvn test` or you can run it from within your IDE if desired.

***
<div id="todo"/>

### TODO

There is a simple emacs org mode file called TODO.org.  I keep track of things that need doing there and check them off as I finish them.

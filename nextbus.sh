#!/bin/sh

cd nextbus-core
mvn -q \
    exec:java \
    -Dexec.mainClass="net.webasap.nextbus.core.Main" \
    -Dexec.args="$@"

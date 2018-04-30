FROM anapsix/alpine-java

MAINTAINER Craig Ching

COPY target/nextbus-service-1.0-SNAPSHOT.jar /nextbus-service.jar
COPY config.yml /config.yml

CMD ["java","-jar","/nextbus-service.jar","server","config.yml"]

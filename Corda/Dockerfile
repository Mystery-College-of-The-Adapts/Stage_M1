FROM ubuntu:16.04

RUN apt-get update && apt-get -y upgrade && \
        apt-get -y install openjdk-8-jre && \
        apt-get -y install openjdk-8-jdk

COPY . /root/corda

WORKDIR /root/corda

RUN ./gradlew deployNodes && \
        kotlin-source/build/nodes/runnodes

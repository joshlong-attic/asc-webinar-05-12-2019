#!/bin/bash

./mvnw clean package
az spring-cloud app deploy -n simple-microservice --jar-path target/service-0.0.1-SNAPSHOT.jar

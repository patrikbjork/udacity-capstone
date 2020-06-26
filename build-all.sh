#!/bin/bash
pushd client/ && npm install && ng build --prod && popd
mvn -f api/pom.xml clean install
docker-compose -f docker/docker-compose-build.yml build

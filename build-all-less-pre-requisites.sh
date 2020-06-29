#!/bin/bash
pushd client/ && npm install && ng build --prod && popd
docker-compose -f docker/docker-compose-build-less-pre-requisites.yml build

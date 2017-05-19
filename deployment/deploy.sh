#!/bin/bash

if [ "$TRAVIS_BRANCH" = 'deploy' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
mvn deploy \
    -DskipTests \
    --settings deployment/settings.xml \
    -Dmaven.wagon.http.ssl.insecure=true
fi

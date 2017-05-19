#!/bin/bash

if [ "$TRAVIS_BRANCH" = 'deploy' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
mvn deploy \
    -DskipTests \
    -Dmaven.wagon.http.ssl.ignore.validity.dates=true \
    -Dmaven.wagon.http.ssl.insecure=true \
    -Dmaven.wagon.http.ssl.allowall=true \
    --settings deployment/settings.xml
fi

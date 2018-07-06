#!/bin/bash

if [ "$TRAVIS_BRANCH" = 'deploy' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
mvn deploy \
    -Psign \
    -DskipTests \
    -X \
    --settings deployment/settings.xml
fi

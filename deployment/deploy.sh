#!/bin/bash

if [ "$LATEST_TAG" != "$CH_VERSION" ]; then
echo "Latest deployed version=$LATEST_TAG not equal new version=$CH_VERSION. Deploying..."
mvn deploy \
    -Psign \
    --settings deployment/settings.xml
else
echo "Skipping release! $LATEST_TAG already released to Nexus! Running tests..."
mvn clean test -T4
fi

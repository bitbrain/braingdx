#!/bin/bash

if [ "$LATEST_TAG" != "$CH_VERSION" ]; then
mvn deploy \
    -Psign \
    --settings deployment/settings.xml
else
echo "Skipping release! $LATEST_TAG already released to Nexus! Running tests..."
mvn clean test
fi

#!/bin/bash

curl -X POST \
-u bitbrain:$GITHUB_TOKEN \
-d "{\
  \"tag_name\": \"$CH_VERSION\",\
  \"target_commitish\": \"$TRAVIS_BRANCH\",\
  \"name\": \"Version $CH_VERSION\",\
  \"body\": \"$CH_TEXT\",\
  \"draft\": false,\
  \"prerelease\": false\
}" https://api.github.com/repos/bitbrain/braingdx/releases

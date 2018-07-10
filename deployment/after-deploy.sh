#!/bin/bash

if [ "$LATEST_TAG" = "$CH_VERSION" ]; then
   echo "Skipping... $LATEST_TAG already released to Github!"
   exit 0
fi

echo "Deploying Javadoc to Github pages..."

# 1. Download freshly from Github
cd $HOME
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=master https://$GITHUB_TOKEN@github.com/bitbrain/braingdx

# 2. Publish new release to Github
source "github-release.sh"

# 3. Push Javadoc to Github
source "github-javadoc.sh"

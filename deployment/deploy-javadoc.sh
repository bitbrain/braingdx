#!/bin/bash

echo "Deploying Javadoc to Github pages..."

# 1. Download freshly from Github
cd $HOME
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=master https://${GITHUB_TOKEN}@github.com/bitbrain/braingdx

# 2. Create temporary dir and build Javadoc with specific version
mkdir cd $HOME/docs
cd $HOME/braingdx
mvn versions:set -DnewVersion=$CH_VERSION && mvn javadoc:javadoc
cd $HOME/docs
cp -r $HOME/braingdx/core/target/site/apidocs/* $HOME/docs
rm -rf $HOME/braingdx/*
cd $HOME/braingdx

# 3. Checkout Jekyll branch and create new folder with new version
git checkout gh-pages
mkdir -p $HOME/braingdx/docs/$CH_VERSION
cp -r $HOME/docs/* $HOME/braingdx/docs/$CH_VERSION

# 4. Copy also into "latest" docs
rm -rf $HOME/braingdx/docs/latest
mkdir -p $HOME/braingdx/docs/latest
cp -r $HOME/docs/* $HOME/braingdx/docs/latest

# Add everything and push!
git add -f *
git commit -m "Travis build $TRAVIS_BUILD_NUMBER - update Javadoc"
git push -fq origin gh-pages && echo "Successfully deployed Javadoc to /docs"

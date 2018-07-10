#!/bin/bash

if [ "$LATEST_TAG" = "$CH_VERSION" ]; then
   echo "Skipping... $LATEST_TAG already released to Github!"
   exit 0
fi

# 1. Download freshly from Github
cd $HOME
git config --global user.email "jarvisdeploybot@gmail.com"
git config --global user.name "Jarvis"
git clone --quiet --branch=master https://${GITHUB_TOKEN}@github.com/bitbrain/braingdx

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

mkdir cd $HOME/docs
cd $HOME/braingdx
mvn versions:set -DnewVersion=$CH_VERSION && mvn javadoc:javadoc
cd $HOME/docs
cp -r $HOME/braingdx/core/target/site/apidocs/* $HOME/docs
rm -rf $HOME/braingdx/*
cd $HOME/braingdx

# 4. Checkout Jekyll branch and create new folder with new version
git checkout gh-pages
mkdir -p $HOME/braingdx/docs/$CH_VERSION
cp -r $HOME/docs/* $HOME/braingdx/docs/$CH_VERSION

# 5. Copy also into "latest" docs
rm -rf $HOME/braingdx/docs/latest
mkdir -p $HOME/braingdx/docs/latest
cp -r $HOME/docs/* $HOME/braingdx/docs/latest

# 6. Add everything and push!
git add -f *
git commit -m "Travis build $TRAVIS_BUILD_NUMBER - update Javadoc"
git push -fq origin gh-pages && echo "Successfully deployed Javadoc to /docs"

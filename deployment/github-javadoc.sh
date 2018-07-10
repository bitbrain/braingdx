#!/bin/bash

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

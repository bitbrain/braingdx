#!/bin/bash

echo "Deploying Javadoc to Github pages"

cd $HOME
git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"
git clone --quiet --branch=master https://${GITHUB_TOKEN}@github.com/bitbrain/braingdx
mkdir cd $HOME/docs
cd $HOME/braingdx
mvn versions:set -DnewVersion=$CH_VERSION && mvn javadoc:javadoc
cd $HOME/docs
cp -r $HOME/braingdx/core/target/site/apidocs/* $HOME/docs
rm -rf $HOME/braingdx/*
cd $HOME/braingdx
git checkout gh-pages
rm -rf $HOME/braingdx/*
cp -r $HOME/docs $HOME/braingdx
git add -f *
git commit -m "Travis build $TRAVIS_BUILD_NUMBER - update Javadoc"
git push -fq origin gh-pages && echo "Successfully deployed Javadoc to /docs"

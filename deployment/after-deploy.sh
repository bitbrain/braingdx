#!/bin/bash

if [ "$LATEST_TAG" = "$CH_VERSION" ]; then
   echo "Skipping... $LATEST_TAG already released to Github!"
   exit 0
fi

# 1. Download freshly from Github
cd $HOME
git config --global user.email "sirlancelbot@gmail.com"
git config --global user.name "Sir Lancelbot"
git clone --quiet --branch=master https://${GITHUB_TOKEN}@github.com/bitbrain/braingdx

# Replacing line endings in body
body=$(sed -E ':a;N;$!ba;s/\r{0,1}\n/\\n/g' <(echo $CH_TEXT))
json='{"tag_name":"'$CH_VERSION'","target_commitish":"'$TRAVIS_BRANCH'","name":"Version '$CH_VERSION'","body":"'$body'","draft":false,"prerelease":false}'

echo "Sending JSON: $json"

curl -X POST \
-u bitbrain:$GITHUB_TOKEN \
-d $json https://api.github.com/repos/bitbrain/braingdx/releases

mkdir cd $HOME/docs
cd $HOME/braingdx
mvn versions:set -DskipTests -DnewVersion=$CH_VERSION -T4 && mvn javadoc:javadoc -DskipTests -T4
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

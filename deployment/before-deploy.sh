#!/bin/bash

openssl aes-256-cbc \
-K $encrypted_8fcb00ef3894_key \
-iv $encrypted_8fcb00ef3894_iv \
-in deployment/codesigning.asc.enc \
-out deployment/codesigning.asc \
-d
cat deployment/codesigning.asc
gpg \
   --fast-import deployment/codesigning.asc

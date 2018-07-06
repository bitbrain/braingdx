#!/bin/bash

openssl aes-256-cbc \
-K $encrypted_041633c72a0c_key \
-iv $encrypted_041633c72a0c_iv \
-in deployment/codesigning.asc.enc \
-out deployment/codesigning.asc \
-d
gpg \
   --fast-import deployment/codesigning.asc

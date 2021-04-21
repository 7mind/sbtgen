#!/usr/bin/env sh

rm -rf ./test/jvm
cp -R target/test-out-jvm test/jvm

rm -rf ./test/js
cp -R target/test-out-js test/js


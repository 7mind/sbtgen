#!/usr/bin/env sh

rm -rf ./test/jvm
cp -R target/test-out-jvm test/jvm

rm -rf ./test/js
cp -R target/test-out-js test/js


rm -rf ./test/sbt2
cp -R target/test-out-sbt2 test/sbt2

#!/bin/bash

set -x
set -e

function csbt {
  COMMAND="time sbt -batch -no-colors -v $*"
  eval $COMMAND
}

function build {
  csbt +clean +test
}

function scripted {
  csbt clean publishLocal sbt-tests/scripted || exit 1
}

function publish {
  #copypaste
  if [[ "$CI_PULL_REQUEST" != "false"  ]] ; then
    return 0
  fi

  if [[ ! -f .secrets/credentials.sonatype-nexus.properties ]] ; then
    return 0
  fi

  if [[ ! ("$CI_BRANCH" == "develop" || "$CI_BRANCH_TAG" =~ ^v.*$ ) ]] ; then
    return 0
  fi

  echo "PUBLISH SCALA LIBRARIES..."

  if [[ "$CI_BRANCH" == "develop" ]] ; then
    csbt +clean +package +publishSigned
  else
    csbt +clean +package +publishSigned sonatypeBundleRelease
  fi
}

function init {
    export SCALA212=$(cat projects/ScalaVersions.scala | grep 'scala_212' |  sed -r 's/.*\"(.*)\".**/\1/')
    export SCALA213=$(cat projects/ScalaVersions.scala | grep 'scala_213' |  sed -r 's/.*\"(.*)\".**/\1/')

    printenv
}

function secrets {
    if [[ "$CI_PULL_REQUEST" == "false"  ]] ; then
        openssl aes-256-cbc -K ${OPENSSL_KEY} -iv ${OPENSSL_IV} -in secrets.tar.enc -out secrets.tar -d
        tar xvf secrets.tar
    fi
}

init

PARAMS=()
SOFT=0
SKIP=()
for i in "$@"
do
case $i in
    nothing)
        echo "Doing nothing..."
    ;;

    build)
        build
    ;;

    scripted)
        scripted
    ;;

    publish)
        publish
    ;;

    secrets)
        secrets
    ;;

    *)
        echo "Unknown option"
        exit 1
    ;;
esac
done

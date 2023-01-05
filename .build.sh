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
    echo "=== INIT ==="
    if [[ "$SYSTEM_PULLREQUEST_PULLREQUESTNUMBER" == ""  ]] ; then
        export CI_PULL_REQUEST=false
    else
        export CI_PULL_REQUEST=true
    fi

    export USERNAME=${USER:-`whoami`}
    export COURSIER_CACHE=${COURSIER_CACHE:-`~/.coursier`}
    export IVY_CACHE_FOLDER=${IVY_CACHE_FOLDER:-`~/.ivy2`}

    export SCALA212=$(cat projects/ScalaVersions.scala | grep 'scala_212' |  sed -r 's/.*\"(.*)\".**/\1/')
    export SCALA213=$(cat projects/ScalaVersions.scala | grep 'scala_213' |  sed -r 's/.*\"(.*)\".**/\1/')

    printenv

    git config --global user.name "$USERNAME"
    git config --global user.email "$CI_BUILD_UNIQ_SUFFIX@$CI_COMMIT"
    git config --global core.sshCommand "ssh -t -o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null"

    echo "pwd: `pwd`"
    echo "Current directory:"
    ls -la .
    echo "Home:"
    ls -la ~

    echo "=== END ==="
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

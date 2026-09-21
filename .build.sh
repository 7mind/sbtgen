#!/bin/bash

set -x
set -e

SONATYPE_SECRET=.secrets/credentials.sonatype-nexus.properties

function csbt {
  COMMAND="time sbt -batch -no-colors -v $*"
  eval $COMMAND
}

function build {
  csbt +clean +test
}

function scripted {
  # cross over both sbt majors: the plugin is published for sbt 1.x and sbt 2.x
  csbt +clean +publishLocal +sbt-tests/scripted || exit 1
}

function publish {
  #copypaste
  if [[ "$CI_PULL_REQUEST" != "false"  ]] ; then
    return 0
  fi

  if [[ ! -f "$SONATYPE_SECRET" ]] ; then
    return 0
  fi


  if [[ ! ("$CI_BRANCH" == "develop" || "$CI_BRANCH_TAG" =~ ^v.*$ ) ]] ; then
    return 0
  fi

  echo "PUBLISH SCALA LIBRARIES..."

  if [[ "$CI_BRANCH" == "develop" ]] ; then
    csbt +clean +package +publishSigned
  else
    csbt +clean +package +publishSigned sonaUpload sonaRelease
  fi
}

function init {
    export SCALA212=$(cat projects/ScalaVersions.scala | grep 'scala_212' |  sed -r 's/.*\"(.*)\".**/\1/')
    export SCALA213=$(cat projects/ScalaVersions.scala | grep 'scala_213' |  sed -r 's/.*\"(.*)\".**/\1/')

    printenv
}

function secrets {
    mkdir .secrets
    if [[ "$CI_PULL_REQUEST" == "false"  ]] ; then
      echo "$SONATYPE_CREDENTIALS_FILE" > "$SONATYPE_SECRET"
      openssl aes-256-cbc -K "$OPENSSL_KEY" -iv "$OPENSSL_IV" -in secrets.tar.enc -out secrets.tar -d
      tar xvf secrets.tar
    fi
}

init

function nixify() {
    if [[ -z "${IN_NIX_SHELL+x}" ]]; then
        echo "[info] Restarting in Nix..."
        export NIXIFIED=1
        nix flake lock
        nix flake metadata
        exec nix develop \
          --ignore-environment \
          --keep HOME \
          --keep NIXIFIED \
          --keep DO_VERBOSE \
          --keep CI \
          --keep CI_BRANCH \
          --keep CI_COMMIT \
          --keep CI_BRANCH_TAG \
          --keep CI_PULL_REQUEST \
          --keep CI_BUILD_UNIQ_SUFFIX \
          --keep OPENSSL_KEY \
          --keep OPENSSL_IV \
          --keep SONATYPE_CREDENTIALS_FILE \
          --command bash .build.sh "$@"
    fi
}

PARAMS=()
SOFT=0
SKIP=()
for i in "$@"
do
case $i in
    nothing)
        echo "Doing nothing..."
    ;;

    --nix)
      idx=$((idx+1))
      shift && nixify "$@"
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

#!/bin/bash

#kubectl -n development apply -f ./deployment/app-deployment.yml


mvn_build() {
  mvn clean install -Dmaven.test.skip=true
}

# shellcheck disable=SC1073
deploy_dev() {
  echo "Deploy to DEV environment"
  docker buildx build -t 750655480130.dkr.ecr.ap-southeast-1.amazonaws.com/doxa-connex-dev/gateway:main-sso . --output=type=docker
  docker push 750655480130.dkr.ecr.ap-southeast-1.amazonaws.com/doxa-connex-dev/gateway:main-sso
}

deploy_stag() {
   echo "Deploy to STAG environment"
  docker buildx build -t 750655480130.dkr.ecr.ap-southeast-1.amazonaws.com/doxa-connex-stag/gateway:latest . --output=type=docker
  docker push 750655480130.dkr.ecr.ap-southeast-1.amazonaws.com/doxa-connex-stag/gateway:latest
}

deploy_uat() {
   echo "Deploy to UAT environment"
 docker buildx build -t 750655480130.dkr.ecr.ap-southeast-1.amazonaws.com/doxa-connex-uat/gateway:latest . --output=type=docker
 docker push 750655480130.dkr.ecr.ap-southeast-1.amazonaws.com/doxa-connex-uat/gateway:latest
}

# Build artifact

mvn_build

case $1 in
"dev")
deploy_dev
  ;;
"stag")
deploy_stag
;;
"uat")
deploy_uat
esac
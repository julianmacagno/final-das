#!/bin/sh

set -x

rm -rf das-*

git submodule update --remote --recursive --merge

cd das-services
mvn clean package
docker buildx build --platform linux/amd64,linux/arm64/v8 -t javiroberts/das-services:latest . --push
cd ..

cd das-entities
mvn clean package
docker buildx build --platform linux/amd64,linux/arm64/v8 -t javiroberts/das-entities:latest . --push
cd ..

cd das-frontend
npm install --legacy-peer-deps
npm run build --prod
docker buildx build --platform linux/amd64,linux/arm64/v8 -t javiroberts/das-frontend:latest . --push
cd ..

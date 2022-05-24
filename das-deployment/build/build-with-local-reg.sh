#!/bin/sh

set -x

rm -rf das-*

git submodule update --remote --recursive --merge

cd das-services
mvn clean package
docker build -t javiroberts/das-services:latest .
cd ..

cd das-entities
mvn clean package
docker build -t javiroberts/das-entities:latest .
cd ..

cd das-frontend
npm install --legacy-peer-deps
npm run build --prod
docker build -t javiroberts/das-frontend:latest .
cd ..

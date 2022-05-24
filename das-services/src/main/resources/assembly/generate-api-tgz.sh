#!/bin/sh
version="$1"

echo "Unzip rescueapp-v"$version".tgz"
rm -rf rescueapp-v$version
mkdir rescueapp-v$version
tar -xvzf rescueapp-v$version.tgz -C rescueapp-v$version

echo "Removing rescueapp-v"$version".tgz"
rm -rf rescueapp-v$version.tgz

echo "Compressing rescueapp-v"$version
tar -czvf rescueapp-v$version.tgz rescueapp-v$version
rm -rf rescueapp-v$version

echo "Copying generated package in frontend project"
cp rescueapp-v$version.tgz ../../das-frontend/local-packages/

echo "Success -> rescueapp-v"$version".tgz created"

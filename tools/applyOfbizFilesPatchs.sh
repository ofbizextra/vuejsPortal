#!/usr/bin/env bash

cd ../../..
patchs="$(find ./plugins/vuejsPortal/ofbizFiles/ -name '*.patch')"
for file in $patchs;
do
pwd
echo "$file"
patch -p0 --verbose < "$file"
done
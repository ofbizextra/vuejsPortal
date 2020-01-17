#!/usr/bin/env bash

cd ../../..
patchs="$(find ./plugins/vuejsPortal/ofbizFiles/ -name '*.patch')"
for file in $patchs;
do
pwd
echo "$file"
tmp="${file%.patch*}"
tmp2="${tmp#*ofbizFiles/}"
git checkout -- "$tmp2"
git apply < "$file"
done
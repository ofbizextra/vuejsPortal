#!/usr/bin/env bash

cd ../../..
patchs="$(find ./plugins/vuejsPortal/ofbizFiles/ -name '*.patch')"
for file in $patchs;
do
pwd
echo "$file"
tmp="${file%.patch*}"
tmp2="${tmp#*ofbizFiles/}"
svn revert "$tmp2"
patch -p0 --verbose < "$file"
done
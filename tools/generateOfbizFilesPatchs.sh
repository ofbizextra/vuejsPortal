#!/usr/bin/env bash

cd ../../..
patchs="$(find ./plugins/vuejsPortal/ofbizFiles/ -name '*.patch')"
for file in $patchs;
do
tmp="${file%.patch*}"
tmp2="${tmp#*ofbizFiles/}"
echo "$tmp2"
git diff "./$tmp2" > "./plugins/vuejsPortal/ofbizFiles/$tmp2.patch"
cat "./plugins/vuejsPortal/ofbizFiles/$tmp2.patch"
done
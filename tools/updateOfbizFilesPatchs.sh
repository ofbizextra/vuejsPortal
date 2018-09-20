#!/usr/bin/env bash

patchs="$(find ./../ofbizFiles/ -name '*.patch')"
for file in $patchs;
do
tmp="${file%.patch*}"
tmp2="${tmp#*ofbizFiles/}"
echo "$tmp2"
svn diff "./../../../$tmp2" > "./../ofbizFiles/$tmp2.patch"
cat "./../ofbizFiles/$tmp2.patch"
done
#! /bin/bash

echo current path: $PWD
currentPath=$PWD

echo "current 分支是线上分支"
echo "next 是当前开发的分支"
read -p "请输入要更新的模板分支：next/current:" next

if test "$next" != "current"
then
next="next"
fi
echo $next

rm -rf dist.tar.gz
rm -rf dist
curl -o dist.tar.gz https://static-1258626455.cos.ap-shanghai.myqcloud.com/app-template/$next.tar.gz
tar -zxvf dist.tar.gz



distFile="$currentPath/NewsDetail/src/main/assets/dist/"

rm -rf $distFile

echo 'cp--- '
cp -rvf dist $distFile


rm -rf dist.tar.gz
rm -rf dist

git add $distFile

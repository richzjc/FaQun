#! /bin/bash

uploadToGit(){
  echo "uploadToGit"
  path=$1
  rename=$2
  rm -rf rename
  cp -a path .
  
}

packageAphrodite(){
echo "开始打包Aphrodite"
./tinker.sh
git pull
echo "检查versionCode , versionName是否更改Y/N"
read -p "已经修改输入Y, 未修改输入N:" value
echo "$value"
if test "$value" = "Y" -o "$value" = "y"; then
echo "versionCode versionName已经修改过了"
else
echo "请修改versionCode versionName"
open gradle.properties
fi
echo "请检查切换环境的开关是否关闭Y/N"
read -p "已经关闭了环境的开关输入Y, 未关闭则输入N:" toggle
if [ "$toggle" = "Y" -o "$toggle" = "y" ]; then
echo "已经关闭了环境的开关"
else
open ./Setting/src/main/java/com/wallstreetcn/setting/Main/SettingActivity.java
fi

echo "请检查ServerApi类里面连接的环境是否为线上环境，Y/N"
read -p "已经是线上环境了输入Y, 不是线上环境则输入N:" env
if [ "$env" = "Y" -o "$env" = "y" ]; then
echo "已经修改为线上环境"
else
open ./WallstreetGlobal/src/main/java/com/wallstreetcn/global/api/ServerAPI.java
fi


echo "修改tinker_support里面的tinkerId为release"
read -p "已修改为release了则输入Y, 未修改则输入N:" isRelease
if [ "$isRelease" = "Y" -o "$isRelease" = "y" ]; then
echo "已经修改为了release"
else
open ./Aphrodite/tinker-support.gradle
fi
rm -rf ./Aphrodite/build
flag=true
while $flag
do
./gradlew clean
./gradlew :Aphrodite:assembleRelease
if [ "$?" -ne 0 ]; then
open ./settings.gradle
else
flag=false
echo "执行成功， 退出循环"
echo "请保留基带包"
echo "打加固包， 首页执行命令：Java -jar jiagu.jar  ， 加固完成后需要重新签名：java -jar apksigner.jar sign --ks wallstreetcn_key.key --ks-key-alias wallstreetcn_key --ks-pass pass:huaerjie001 --key-pass pass:huaerjie001 --out output.apk input.apk
"
cp -a ./Aphrodite/build/bakApk "$HOME/Desktop/"
break
fi
done
}

packageAres(){
echo "开始打包Ares"
./tinker.sh
git pull
echo "检查versionCode , versionName是否更改Y/N"
read -p "已经修改输入Y, 未修改输入N:" value
echo "$value"
if test "$value" = "Y" -o "$value" = "y"; then
echo "versionCode versionName已经修改过了"
else
echo "请修改versionCode versionName"
open gradle.properties
fi
echo "请检查切换环境的开关是否关闭Y/N"
read -p "已经关闭了环境的开关输入Y, 未关闭则输入N:" toggle
if [ "$toggle" = "Y" -o "$toggle" = "y" ]; then
echo "已经关闭了环境的开关"
else
open ./Setting/src/main/java/com/wallstreetcn/setting/Main/SettingActivity.java
fi

echo "请检查ServerApi类里面连接的环境是否为线上环境，Y/N"
read -p "已经是线上环境了输入Y, 不是线上环境则输入N:" env
if [ "$env" = "Y" -o "$env" = "y" ]; then
echo "已经修改为线上环境"
else
open ./WallstreetGlobal/src/main/java/com/wallstreetcn/global/api/ServerAPI.java
fi


echo "修改tinker_support里面的tinkerId为release"
read -p "已修改为release了则输入Y, 未修改则输入N:" isRelease
if [ "$isRelease" = "Y" -o "$isRelease" = "y" ]; then
echo "已经修改为了release"
else
open ./Ares/tinker-support.gradle
fi
rm -rf ./Ares/build
flag=true
while $flag
do
./gradlew clean
./gradlew :Ares:assembleRelease
if [ "$?" -ne 0 ]; then
open ./settings.gradle
else
flag=false
echo "执行成功， 退出循环"
echo "请保留基带包"
echo "打加固包， 首页执行命令：Java -jar jiagu.jar  ， 加固完成后需要重新签名：java -jar apksigner.jar sign --ks wallstreetcn_key.key --ks-key-alias wallstreetcn_key --ks-pass pass:huaerjie001 --key-pass pass:huaerjie001 --out output.apk input.apk
"
cp -a ./Ares/build/bakApk/. "$HOME/Desktop/"
break
fi
done
}

packageHephaistos(){
echo "开始打包Hephaistos"
./tinker.sh
git pull
echo "检查versionCode , versionName是否更改Y/N"
read -p "已经修改输入Y, 未修改输入N:" value
echo "$value"
if test "$value" = "Y"; then
echo "versionCode versionName已经修改过了"
else
echo "请修改versionCode versionName"
open gradle.properties
fi
echo "请检查切换环境的开关是否关闭Y/N"
read -p "已经关闭了环境的开关输入Y, 未关闭则输入N:" toggle
if [ "$toggle" = "Y" -o "$toggle" = "y" ]; then
echo "已经关闭了环境的开关"
else
open ./Setting/src/main/java/com/wallstreetcn/setting/Main/SettingActivity.java
fi

echo "请检查ServerApi类里面连接的环境是否为线上环境，Y/N"
read -p "已经是线上环境了输入Y, 不是线上环境则输入N:" env
if [ "$env" = "Y" -o "$env" = "y" ]; then
echo "已经修改为线上环境"
else
open ./WallstreetGlobal/src/main/java/com/wallstreetcn/global/api/ServerAPI.java
fi

echo "修改tinker_support里面的tinkerId为release"
read -p "已修改为release了则输入Y, 未修改则输入N:" isRelease
if [ "$isRelease" = "Y" -o "$isRelease" = "y" ]; then
echo "已经修改为了release"
else
open ./Hephaistos/tinker-support.gradle
fi
rm -rf ./Hephaistos/build
flag=true
while $flag
do
./gradlew clean
./gradlew :Hephaistos:assembleRelease
if [ "$?" -ne 0 ]; then
open ./settings.gradle
else
flag=false
echo "执行成功， 退出循环"
echo "请保留基带包"
echo "打加固包， 首页执行命令：Java -jar jiagu.jar  ， 加固完成后需要重新签名：java -jar apksigner.jar sign --ks wallstreetcn_key.key --ks-key-alias wallstreetcn_key --ks-pass pass:huaerjie001 --key-pass pass:huaerjie001 --out output.apk input.apk
"
cp -a ./Hephaistos/build/bakApk "$HOME/Desktop/"
break
fi
done
}

packageApollo(){
echo "开始打包Apollo"
git pull
echo "检查versionCode , versionName是否更改Y/N"
read -p "已经修改输入Y, 未修改输入N:" value
echo "$value"
if test "$value" = "Y"; then
echo "versionCode versionName已经修改过了"
else
echo "请修改versionCode versionName"
open gradle.properties
fi
echo "请检查切换环境的开关是否关闭Y/N"
read -p "已经关闭了环境的开关输入Y, 未关闭则输入N:" toggle
if [ "$toggle" = "Y" -o "$toggle" = "y" ]; then
echo "已经关闭了环境的开关"
else
open ./Setting/src/main/java/com/wallstreetcn/setting/Main/SettingActivity.java
fi

echo "请检查ServerApi类里面连接的环境是否为线上环境，Y/N"
read -p "已经是线上环境了输入Y, 不是线上环境则输入N:" env
if [ "$env" = "Y" -o "$env" = "y" ]; then
echo "已经修改为线上环境"
else
open ./WallstreetGlobal/src/main/java/com/wallstreetcn/global/api/ServerAPI.java
fi

rm -rf ./Apollo/build
flag=true
while $flag
do
./gradlew clean
./gradlew assembleRelease Apollo
if [ "$?" -ne 0 ]; then
open ./settings.gradle
else
flag=false
echo "执行成功， 退出循环"
echo "请保留基带包"
echo "打加固包， 首页执行命令：Java -jar jiagu.jar  ， 加固完成后需要重新签名：java -jar apksigner.jar sign --ks wallstreetcn_key.key --ks-key-alias wallstreetcn_key --ks-pass pass:huaerjie001 --key-pass pass:huaerjie001 --out output.apk input.apk
"
echo "打加固包， 首页执行命令：Java -jar jiagu.jar  ， 加固完成后需要重新签名：java -jar apksigner.jar sign --ks wallstreetcn_key.key --ks-key-alias wallstreetcn_key --ks-pass pass:huaerjie001 --key-pass pass:huaerjie001 --out output.apk input.apk"
cp -a ./Apollo/build/bakApk/. "$HOME/Desktop/"
break
fi
done
}

packageGlaucus(){
echo "开始打包Glaucus"
git pull
echo "检查versionCode , versionName是否更改Y/N"
read -p "已经修改输入Y, 未修改输入N:" value
echo "$value"
if test "$value" = "Y"; then
echo "versionCode versionName已经修改过了"
else
echo "请修改versionCode versionName"
open gradle.properties
fi
echo "请检查切换环境的开关是否关闭Y/N"
read -p "已经关闭了环境的开关输入Y, 未关闭则输入N:" toggle
if [ "$toggle" = "Y" -o "$toggle" = "y" ]; then
echo "已经关闭了环境的开关"
else
open ./Setting/src/main/java/com/wallstreetcn/setting/Main/SettingActivity.java
fi

echo "请检查ServerApi类里面连接的环境是否为线上环境，Y/N"
read -p "已经是线上环境了输入Y, 不是线上环境则输入N:" env
if [ "$env" = "Y" -o "$env" = "y" ]; then
echo "已经修改为线上环境"
else
open ./WallstreetGlobal/src/main/java/com/wallstreetcn/global/api/ServerAPI.java
fi


rm -rf ./Apollo/build
flag=true
while $flag
do
./gradlew clean
./gradlew :Glaucus:assembleRelease
if [ "$?" -ne 0 ]; then
open ./settings.gradle
else
flag=false
echo "执行成功， 退出循环"
echo "请保留基带包"
echo "打加固包， 首页执行命令：Java -jar jiagu.jar  ， 加固完成后需要重新签名：java -jar apksigner.jar sign --ks wallstreetcn_key.key --ks-key-alias wallstreetcn_key --ks-pass pass:huaerjie001 --key-pass pass:huaerjie001 --out output.apk input.apk"
cp -a ./Glaucus/build/output/apk "$HOME/Desktop/"
break
fi
done
}

packageHorae(){
echo "开始打包Horae"
git pull
echo "检查versionCode , versionName是否更改Y/N"
read -p "已经修改输入Y, 未修改输入N:" value
echo "$value"
if test "$value" = "Y"; then
echo "versionCode versionName已经修改过了"
else
echo "请修改versionCode versionName"
open gradle.properties
fi
echo "请检查切换环境的开关是否关闭Y/N"
read -p "已经关闭了环境的开关输入Y, 未关闭则输入N:" toggle
if [ "$toggle" = "Y" -o "$toggle" = "y" ]; then
echo "已经关闭了环境的开关"
else
open ./Setting/src/main/java/com/wallstreetcn/setting/Main/SettingActivity.java
fi

echo "请检查ServerApi类里面连接的环境是否为线上环境，Y/N"
read -p "已经是线上环境了输入Y, 不是线上环境则输入N:" env
if [ "$env" = "Y" -o "$env" = "y" ]; then
echo "已经修改为线上环境"
else
open ./WallstreetGlobal/src/main/java/com/wallstreetcn/global/api/ServerAPI.java
fi

rm -rf ./Apollo/build
flag=true
while $flag
do
./gradlew clean
./gradlew :Horae:assembleRelease
if [ "$?" -ne 0 ]; then
open ./settings.gradle
else
flag=false
echo "执行成功， 退出循环"
echo "请保留基带包"
echo "打加固包， 首页执行命令：Java -jar jiagu.jar  ， 加固完成后需要重新签名：java -jar apksigner.jar sign --ks wallstreetcn_key.key --ks-key-alias wallstreetcn_key --ks-pass pass:huaerjie001 --key-pass pass:huaerjie001 --out output.apk input.apk"
cp -a ./Aphrodite/build/bakApk "$HOME/Desktop/"
break
fi
done
}

cd /Users/zhangjianchuan/project/android/Prometheus/News

echo $PWD
echo "请选择要打包Module"
echo "1. Aphrodite(普通版本)"
echo "2. Hephaistos(华为pro版本)"
echo "3. Apollo(华为海外版本)"
echo "4. Glaucus(谷歌版本)"
echo "5. Horae(应用宝版本)"
echo "6. Ares(见闻VIP版本)"
read -p "请输入打包前面的序号:" number
echo $number
if test "$number" = "1"
then
packageAphrodite
elif [ "$number" = "2" ]
then
packageHephaistos
elif [ "$number" -eq 3 ]; then
packageApollo
elif test "$number" -eq 4
then
packageGlaucus
elif [ "$number" = "5" ]; then 
packageHorae
elif [ "$number" = "6" ]; then 
packageAres
else 
echo "请输入正确的编号"
fi

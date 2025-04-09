#! /bin/bash

package='java -jar ../packer-ng-2.0.0.jar generate'
channelsAres(){
  channelsFile=Ares/markets.txt
  apkFile=Ares/build/bakApk/$1/Ares-release.apk
  rm -rf  Ares/build/archives
  $package --channels=@$channelsFile --output=Ares/build/archives $apkFile
  rm -rf Ares/build/archives.zip
  zip -q -r Ares/build/archives.zip Ares/build/archives/
}

echo $PWD
echo "多渠道打包"
echo "请选择要打包Module"
echo "1. Aphrodite(普通版本)"
echo "2. Hephaistos(华为pro版本)"
echo "3. Apollo(华为海外版本)"
echo "4. Glaucus(谷歌版本)"
echo "5. Horae(应用宝版本)"
echo "6. Ares(见闻VIP版本)"
read -p "请输入打包前面的序号:" number
read -p "输入基础包目录：" Directory

echo $number
if test "$number" = "6"
then
channelsAres $Directory
fi

#验证
#java -jar ../packer-ng-2.0.0.jar verify Ares/archives/Ares-release-xiaomi.ap

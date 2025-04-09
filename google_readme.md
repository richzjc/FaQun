申请微信账号信息
Facebook 账号信息

google play版本
包名： com.wallstreetcn.news.googleplay
签名： b2aa75f035862d04417bb5458e5ab725   本机签名
      50e8f3f05e8252fc94aa9ef54ad7affd   google 签名


QQ：
APP ID：1106993055
APP KEY：bMGhEYLZRCVhFBE9

微博：
App Key：2344029974
App Secret：76b133d430f1433f106acf86264cfaf4


华尔街见闻微博账号
http://open.weibo.com/apps
panzhiyao@wallstreetcn.com
zhuchen1803

Google版：
包名：com.wallstreetcn.news.googleplay
小米：
appId : 2882303761517842357
Appkey : 5471784263357
AppSercret : LgntLAMxfuFmF9OMKZ2raQ==

华为：
APPId ： 100358979
APPSecret： 3683aeb67dd0a4ffe14c5490f2d98b9d

umeng ：
Appkey : 5b5a7fbfb27b0a2b270002bd
App Master Secret : oysrmd7so2jlslcqgagigwwlwudirloa
Umeng Message Secret : 0f532cf89302770809001d924b84f190


见闻应用宝账号
http://open.qq.com/
1462924510
huaerjieCN,2016

360加固：
13127569239
huaerjie2020

加固打包步骤：打包 release -> 加固(不要签名) -> 用一下命令签名 -> 渠道包分发

Java -jar jiagu.jar
加固 生成加固后的包 .jiagu.apk
然后签名
java -jar apksigner.jar sign --ks wallstreetcn_key.key --ks-key-alias wallstreetcn_key --ks-pass pass:huaerjie001 --key-pass pass:huaerjie001 --out output.apk input.apk
验证：验证签名，验证多渠道





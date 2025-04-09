# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/micker/Documents/==tools/android-sdk-macosx/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
# http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
# public *;
#}
-optimizationpasses 5 # 指定代码的压缩级别
-dontusemixedcaseclassnames # 是否使用大小写混合
-dontskipnonpubliclibraryclasses# 是否混淆第三方jar
-dontpreverify# 混淆时是否做预校验
-keepattributes SourceFile,LineNumberTable										# 混淆号错误信息里带上代码行
-verbose# 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*# 混淆时所采用的算法
-ignorewarnings
-renamesourcefileattribute SourceFile

-keepclasseswithmembernames class * { # 保持 native 方法不被混淆
native <methods>;
}
-keepclasseswithmembernames class * implements android.view.View{ *; }
-keepclassmembers class * implements android.view.View{ *; }
-keep class * implements android.view.View{ *; }
-keep class tv.danmaku.ijk.**{*;}
-keep class * implements com.wallstreetcn.webview.model.JavascriptFunction{*;}

#保证实体类不被混淆
-keep class * implements  android.support.v4.app.Fragment{ *; }
-keep class * implements android.os.Parcelable{ *; }
-keep class * extends androidx.lifecycle.ViewModel{ *; }
-keepclasseswithmembernames class * implements android.os.Parcelable{ *; }
-keepclassmembers class * implements android.os.Parcelable {*; }
-keep class * implements com.wallstreetcn.baseui.model.BaseListModel{ *; }
-keepclasseswithmembernames class * implements com.wallstreetcn.baseui.model.BaseListModel{ *; }
-keepclassmembers class * implements com.wallstreetcn.baseui.model.BaseListModel{*; }

#Glide
-keep class com.bumptech.glide.**{*;}
-keep class com.wallstreetcn.news.lazyload.GlideWrapConfiguration{*;}
-keep class * implements com.bumptech.glide.module.GlideModule{ *; }

#Gson
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

# umeng
-dontwarn com.umeng.**
-keep class com.umeng.**{*;}
-keep class com.umeng.message.* {
public <fields>;
public <methods>;
}
-keep class com.umeng.message.protobuffer.* {
public <fields>;
public <methods>;
}
-keep class com.squareup.wire.* {
public <fields>;
public <methods>;
}
-keep class com.umeng.message.local.* {
public <fields>;
public <methods>;
}
-keep class org.android.agoo.impl.*{
public <fields>;
public <methods>;
}
-keep class org.android.agoo.service.* {*;}
-keep class org.android.spdy.**{*;}

#友盟分享
-dontusemixedcaseclassnames
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn com.squareup.okhttp.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes EnclosingMethod
-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.mob.socialize.**
-keep public interface com.mob.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.mob.socialize.* {*;}

-keep class com.umeng.** {*;}
-keep class com.umeng.**
-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.mob.socialize.sensor.**
-keep class com.mob.socialize.handler.**
-keep class com.mob.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep class com.kakao.** {*;}
-dontwarn com.kakao.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.mob.socialize.impl.ImageImpl {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keepattributes Signature


#友盟推送
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn org.apache.thrift.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}
-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class com.wallstreetcn.alien.R$*{
   public static final int *;
}

##避免log打印输出
# -assumenosideeffects class android.util.Log {
#      public static *** v(...);
#      public static *** d(...);
#      public static *** i(...);
#      public static *** w(...);
# }

#下拉刷新类库
-keep class in.srain.**{*;}
-keepclassmembernames class in.srain.**{*;}


#weex
-keep class com.wallstreetcn.weex.WeexContext {*;}
-keep class com.wallstreetcn.weex.utils.WeexUtil {
public static void setLoginInfo(java.lang.String);
public static boolean isUserLogin();
public static void exit();
}
-keep class com.wallstreetcn.weex.ui.news.WeexNewsDetailPresenter {
public <methods>;
}
-keep interface com.wallstreetcn.weex.ui.news.WeexNewsDetailView{*;}
-keep interface com.wallstreetcn.weex.ui.news.WeexNewsDetailPresenter$ArticleCallback{*;}
-keep class com.wallstreetcn.weex.widget.rotateview.PostRotateBean{*;}
-keep class com.wallstreetcn.weex.widget.rotateview.WeexRotateImageView{
public <methods>;
}
-keep class com.wallstreetcn.weex.widget.rotateview.WeexRotateImageView.PostRotateBean{*;}
-keep class * extends com.wallstreetcn.weex.entity.WeexEntity{*;}
-keepattributes JavascriptInterface
-keep class com.wallstreetcn.weex.ui.web.WeexJsOperation {*;}
-keep class com.wallstreetcn.weex.rest.ApiUtil {*;}
-keep class com.wallstreetcn.weex.rest.api.**{*;}
-keep class org.greenrobot.eventbus.**{*;}
-keep class com.trello.rxlifecycle.**{*;}

# rxjava
-dontwarn rx.**
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}
# rxjava2
-dontwarn io.reactivex.**
-keep class io.reactivex.schedulers.Schedulers {
    public static <methods>;
}
-keep class io.reactivex.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.TestScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class io.reactivex.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class io.reactivex.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

##talking data
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}

##umeng 统计
-keepclassmembers class * {
 public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}
-keep public class com.wallstreetcn.news.R$*{
public static final int *;
}

##多渠道打包
-keep class com.mcxiaoke.packer.**{*;}

##阿里百川反馈
-keep class com.alibaba.sdk.android.feedback.impl.FeedbackServiceImpl {*;}
-keep class com.alibaba.sdk.android.feedback.impl.FeedbackAPI {*;}
-keep class com.alibaba.sdk.android.feedback.util.IWxCallback {*;}
-keep class com.alibaba.sdk.android.feedback.util.IUnreadCountCallback{*;}
-keep class com.alibaba.sdk.android.feedback.FeedbackService{*;}
-keep public class com.alibaba.mtl.TLog.model.LogField {public *;}
-keep class com.taobao.securityjni.**{*;}
-keep class com.taobao.wireless.security.**{*;}
-keep class com.ut.secbody.**{*;}
-keep class com.taobao.dp.**{*;}
-keep class com.alibaba.wireless.security.**{*;}
-keep class com.ta.utdid2.device.**{*;}

#语音播报
-keep class com.wallstreetcn.voicecloud.** {*;}

#科大讯飞
-keep class com.iflytek.**{*;}
-keep interface com.iflytek.**{*;}
-keepattributes Signature
-keepattributes *Annotation*


# ProGuard configurations for Bugtags
-keepattributes LineNumberTable,SourceFile

-keep class com.bugtags.library.** {*;}
-dontwarn com.bugtags.library.**
-keep class io.bugtags.** {*;}
-dontwarn io.bugtags.**
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient

#金证
-dontwarn com.bairuitech.**
-keep class com.bairuitech.**{*;}
-dontwarn com.itrus.raapi.**
-keep class com.itrus.raapi.**{*;}
-dontwarn com.org.apache.**
-keep class com.org.apache.**{*;}
-dontwarn org.apache.**
-keep class org.apache.**{*;}
-dontwarn org.**
-keep class org.**{*;}
-dontwarn com.google.**
-keep class com.google.**{*;}
-dontwarn com.kwl.common.utils.**
-keep class com.kwl.common.utils.**{*;}
-dontwarn com.kwl.common.utils.**
-keep class com.kwl.common.utils.**{*;}
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.kwlstock.sdk.**
-keep class com.kwlstock.sdk.**{*;}
-dontwarn com.pakh.video.sdk.**
-keep class com.pakh.video.sdk.**{*;}
-keep class util.**{*;}
-keep class com.openacc.**{*;}
-keep class com.pakh.**{*;}
-keep class com.pingan.**{*;}


# End Bugtags

# 华为pro
-keep class com.huawei.android.sdk.drm.**{*;}

-keep class com.huawei.android.pushagent.**{*;}
-keep class com.huawei.android.pushselfshow.**{*;}
-keep class com.huawei.android.microkernel.**{*;}
-keep class com.baidu.mapapi.**{*;}

#支付宝
-keep class com.alipay.**{*;}

# router
-keep class com.kronos.router.**{*;}
-keep class com.wallstreetcn.baseui.customView.FragmentTabHost{*;}
-keep class com.wallstreetcn.baseui.customView.FragmentTabHost$*{*;}

# bugly

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
# tinker混淆规则
-dontwarn com.tencent.tinker.**
-keep class com.tencent.tinker.** { *; }

-dontwarn com.tencent.**
-keep class com.tencent.**{*;}
-keep class com.tencent.tinker.**{*;}
-keep class * implements com.tencent.tinker.loader.app.DefaultApplicationLike
-keep class * implements android.app.Application
-keep public class * implements com.tencent.tinker.loader.app.ApplicationLiefCycle{*;}
-keep class com.tencent.tinker.loader.** {*;}
-keep public class * extends com.tencent.tinker.loader.TinkerLoader {*;}
-keep public class * extends com.tencent.tinker.loader.app.TinkerApplication {*;}



# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}
#NGPACKER
-keep class com.mcxiaoke.**{*;}

-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
-dontwarn com.facebook.infer.**
-dontwarn com.wallstreetcn.**

# 华为推送
-keep class com.huawei.**{*;}
-keep class com.wallstreetcn.alien.Root.PushProxyActivity{*;}
-keep public class * extends android.content.BroadcastReceiver

#rxpermissions
-keep class com.tbruyelle.**{*;}

#retrofit2

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

#proto buf
-keep class com.google.protobuf.** { *; }
-keep public class * extends com.google.protobuf.** { *; }
-keep class Tanx.** {*;}

#华为账号服务
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.huawei.hianalytics.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep class com.huawei.gamebox.plugin.gameservice.**{*;}

-keep public class com.huawei.android.hms.agent.** extends android.app.Activity { public *; protected *; }
-keep interface com.huawei.android.hms.agent.common.INoProguard {*;}
-keep class * extends com.huawei.android.hms.agent.common.INoProguard {*;}

#pdfviewer
-keep class com.shockwave.**

#feedback
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-keep class com.ut.** {*;}
-dontwarn com.ut.**
-keep class com.ta.** {*;}
-dontwarn com.ta.**

#钉钉
-keep class com.android.dingtalk.share.ddsharemodule.** {*;}

#WEIBO
-keep public class com.sina.weibo.sdk.**{*;}

#天御
-keep public class com.token.verifysdk{*; }

-keep class kotlin.reflect.**{*;}
-keep class kotlin.** { *; }
-keep class org.jetbrains.** { *; }

#应用宝付费sdk
-keep class com.tencent.qqdownloader.pay.** {*;}

#闪验sdk
#-dontwarn com.cmic.sso.sdk.**
#-dontwarn com.sdk.**
#-keep class com.cmic.sso.sdk.**{*;}
#-keep class com.sdk.** { *;}
#-keep class cn.com.chinatelecom.account.api.**{*;}

# 阿里云一键登录
-keep class cn.com.chinatelecom.gateway.lib.** {*;}
-keep class com.unicom.xiaowo.login.** {*;}
-keep class com.cmic.sso.sdk.** {*;}
-keep class com.mobile.auth.** {*;}
-keep class android.support.v4.** { *;}
-keep class org.json.**{*;}
-keep class com.alibaba.fastjson.** {*;}

-keep class **.R$* {  *;  }

# picture_select_lib

#PictureSelector 2.0
-keep class com.luck.picture.lib.** { *; }

-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

 #rxjava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#rxandroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}


# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule


#vlayout
-keepattributes InnerClasses
-keep class com.alibaba.android.vlayout.ExposeLinearLayoutManagerEx { *; }
-keep class android.support.v7.widget.RecyclerView$LayoutParams { *; }
-keep class android.support.v7.widget.RecyclerView$ViewHolder { *; }
-keep class android.support.v7.widget.ChildHelper { *; }
-keep class android.support.v7.widget.ChildHelper$Bucket { *; }
-keep class android.support.v7.widget.RecyclerView$LayoutManager { *; }


-keep class com.kronos.router.fragment.* { *; }
-keep public class * implements com.kronos.router.fragment.IFragmentRouter { *; }
-keep public class com.wallstreetcn.baseui.adapter.BaseFragmentAdapter { *; }
-keep public class com.wallstreetcn.baseui.adapter.BaseViewPage2FragmentAdapter { *; }
-keep public class com.wallstreetcn.baseui.adapter.BaseViewPage2ActivityAdapter { *; }

-keep class com.wallstreetcn.news.huaweipay.init.* { *; }


-keep class com.wallstreetcn.baseui.adapter.BaseRecycleViewHolder
-keep class * extends com.wallstreetcn.baseui.adapter.BaseRecycleViewHolder

-keep class **$$Parameter { *; }
-keepclasseswithmembernames class * {
     @com.richzjc.annotation.* <fields>;
 }

-keepclasseswithmembernames class * {
     @com.richzjc.netannotation.* <methods>;
 }
#行情
-keep class com.wscn.marketlibrary.**{*;}

#A股
-keep class me.wangyuwei.thoth.**{*;}
# 以下是投屏的混淆配置

###plist
-keep class com.dd.plist.** { *; }
-dontwarn com.dd.plist.**
###kxml
-keep class org.kxml2.** { *; }
-keep class org.xmlpull.** { *; }
-dontwarn org.kxml2.**
-dontwarn org.xmlpull.**
###Lebo
-keep class com.hpplay.**{*;}
-keep class com.hpplay.**$*{*;}
-dontwarn com.hpplay.**

# appsflyer
-dontwarn com.android.installreferrer
-keep class com.appsflyer.** { *; }


#添加了混淆
-keep class com.richzjc.library.TabLayout{*;}
-keep class com.richzjc.library.TabLayout$Tab{*;}
-keep class com.richzjc.library.TabLayout$TabView{*;}

# carkit
-keep class com.huawei.hicarsdk.** { *; }
-keep class com.huawei.hicar.** { *; }

-keep class androidx.appcompat.app.** { *; }


# oppo推送接入
-keep public class * extends android.app.Service
-keep class com.heytap.msp.** { *;}

# vivo推送
-dontwarn com.vivo.push.**
-keep class com.vivo.push.**{*; }
-keep class com.vivo.vms.**{*; }
#-keep class com.wallstreetcn.alien.lazyload.push.VivoPushReceiver{*;}
-keep public class * implements com.vivo.push.sdk.OpenClientPushMessageReceiver { *; }


# quotes model
-dontwarn com.wallstreetcn.quotes.longhubang.model.**
-keep class com.wallstreetcn.quotes.longhubang.model.** {*;}
-dontwarn com.wallstreetcn.quotes.model.**
-keep class com.wallstreetcn.quotes.model.** {*;}




-keepnames class * extends android.view.View
-keepnames class * extends android.app.Fragment
-keepnames class * extends android.support.v4.app.Fragment
-keepnames class * extends androidx.fragment.app.Fragment
-keep class android.support.v4.view.ViewPager{
  *;
}
-keep class android.support.v4.view.ViewPager$**{
  *;
}
-keep class androidx.viewpager.widget.ViewPager{
  *;
}
-keep class androidx.viewpager.widget.ViewPager$**{
  *;
}

# jpush
 -dontoptimize
    -dontpreverify

    -dontwarn cn.jpush.**
    -keep class cn.jpush.** { *; }
    -keep class * extends cn.jpush.android.service.JPushMessageService { *; }

    -dontwarn cn.jiguang.**
    -keep class cn.jiguang.** { *; }

# 升级华为推送
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.huawei.hianalytics.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}


-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class com.mob.**{*;}
-keep class com.bytedance.**{*;}
-dontwarn cn.sharesdk.**
-dontwarn com.sina.**
-dontwarn com.mob.**


-keep class com.wallstreetcn.alien.lazyload.push.WSCNMiuiMessageReceiver {*;}
-keep class com.wallstreetcn.quotes.business.ui.viewmodel.QuoteShareViewModel { *; }
#-keep public class tv.danmaku.ijk.media.widget.media.PlayerMediaController{*;}


-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hihonor.push.**{*;}




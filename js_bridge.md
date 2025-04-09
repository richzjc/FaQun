### JS Bridge 方法汇总



公共：
Load:

```text
    audioPlay
    loadImage
    loadUrl
    loadTextFont
    loadDownload
    // audioDownload  删除
    loadToken
    loadApi
    loadResourceLength
    loadTheme
    changeFont
    getAttachment
    loadAd(channel, topic_id?, article_id?, theme_id)
```

Camera
Dialogs

    toast
    alert
Share

```text
web
webShareTo
inviteReading
```

Show

```text
showImage
```

WebView

```text
	open
  close
  closeAll
  load
  cookie
  saveImage
  pay
  trace
  isLogin
```



模板一
NewsDetail:

```text
	init
    judgeFollow
	moreComments
	changeFont -> Load.changeFont
	dismissPlatform -> Load.dismissPlatform
	loadExtra  -> Load.loadAd
	loadData -> NewsDetail.ini
	~~loadTheme~~ 使用Load.loadTheme
	~~audioPlay~~ 使用 audioPlay
	videoPlay
	readPercent -> Load.markScrollPosition(percent)
	judgeSubscription
	judgeLike
	letsPay -> Load.letsPay
	loadComments
	loadMemberArticleImage
	~~audioSize~~ 使用Load.loadResourceLength
	~~loadDownload~~ 使用Load.loadDownload
	~~audioDownload~~ 使用Load.audioDownload  只下载资源 需要与文章信息关联的话 依然保留
	shareNewsLive
	getAttchment -> Load.getAttachment  注意单词拼写
	articleLike
	~~trace~~ 使用WebView.trace
	showBaikeEntry
	unlockFreeQuantity
	paySubscription
```

模板二
PaidMedia:  重构后删除

```text
	init -> NewsDetail.init
	unlockFreeQuantity  -> NewsDetail.unlockFreeQuantity
	antiFake -> Load.antiFake
	videoPlay -> NewsDetail.videoPlay
	paySubscription -> NewsDetail.paySubscription
	markScrollPosition -> Load.markScrollPosition(percent)
	~~audioPlay~~ > Load.audioPlay
	showBaikeEntry  -> NewsDetail.showBaikeEntry
	~~loadDownload~~ 使用Load.loadDownload
	~~audioDownload~~ 使用Load.audioDownload
```


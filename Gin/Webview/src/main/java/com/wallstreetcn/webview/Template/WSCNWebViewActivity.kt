package com.wallstreetcn.webview.Template

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.wallstreetcn.baseui.base.BaseActivity
import com.wallstreetcn.baseui.base.BasePresenter
import com.wallstreetcn.baseui.manager.AudioPlayManager
import com.wallstreetcn.baseui.widget.pulltorefresh.IRefreshListener
import com.wallstreetcn.helper.utils.FragmentHelper
import com.wallstreetcn.helper.utils.ResourceUtils
import com.wallstreetcn.helper.utils.rx.RxUtils
import com.wallstreetcn.helper.utils.system.ScreenUtils
import com.wallstreetcn.helper.utils.system.WindowHelper
import com.wallstreetcn.webview.R
import com.wallstreetcn.webview.databinding.WebviewActivityWscnWebBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

open class WSCNWebViewActivity : BaseActivity<Any, BasePresenter<Any>>(), IRefreshListener,
    WebLoadCallback {

    companion object {
        const val KEY_SHOWSHARE = "isShowShare"
    }

    val mWeb by lazy {
        val fragment = WSCNWebViewFragment_()
        fragment.arguments = intent?.extras
        fragment.loadCallback = this
        fragment
    }

//    private var webView: WSCNWebView? = null

    override fun doInitData() {
        setStatusBarTranslucentCompat()
        super.doInitData()
        intent?.extras?.apply {
            binding!!.titleBar.title = getString("title", "")

            val url = getString("url")
            checkFullscreen(url)
            setStatusBarLight(url)
            checkIsShowShareBtn(getIsShowShareBtn(this))
        }
    }


    private fun getIsShowShareBtn(bundle: Bundle?): Boolean {
        val url = bundle?.getString("url")
        if (TextUtils.isEmpty(url)) {
            return true
        }
        try {
            val uri = Uri.parse(url)
            if (TextUtils.equals(uri.scheme, "file")) {
                return false
            }
            val isShowShareQuery = uri.getQueryParameter(KEY_SHOWSHARE)
            if (!TextUtils.isEmpty(isShowShareQuery)) {
                return uri.getBooleanQueryParameter(KEY_SHOWSHARE, true)
            }
            return bundle!!.getBoolean(KEY_SHOWSHARE, true)
        } catch (e: Exception) {
            e.printStackTrace()
            return true
        }
    }

    override fun doInitSubViews(view: View?) {
        super.doInitSubViews(view)
        binding!!.titleBar.setIconBackOnclickListener {
            onBackPressed()
        }
        binding!!.deleteIv.setOnClickListener { finish() }
        AudioPlayManager.setCoordinatorLayout(binding!!.coordinator)
        FragmentHelper.replaceFragment(supportFragmentManager, R.id.fragment_content, mWeb)
    }

    override fun onRefresh() {
        mWeb.onRefresh()
    }

    override fun onLoadFinish() {

    }

    override fun onLoadTitle(title: String?) {
        title?.apply {
            binding!!.titleBar.title = title
        }
    }

    private fun checkFullscreen(url: String?) {
        if (isFullScreen(url)) {
            binding!!.titleBar.visibility = View.GONE
            binding!!.titleBarLine.visibility = View.GONE
            binding!!.titleLayout.visibility = View.GONE
            checkLightStatusBar()
        } else {
            binding!!.titleBar.visibility = View.VISIBLE
            binding!!.titleBarLine.visibility = View.VISIBLE
            binding!!.titleLayout.visibility = View.VISIBLE
        }
    }

    private fun isFullScreen(url: String?): Boolean {
        if (!TextUtils.isEmpty(url)) {
            val uri = Uri.parse(url)
            val para = uri.queryParameterNames
            if (para != null && para.contains("wscnfullscreen")
                && TextUtils.equals(uri.getQueryParameter("wscnfullscreen"), "1")
            ) {
                if (para.contains("showStatus")) {
                    val statusBarLight = uri.getQueryParameter("showStatus")
                    if (TextUtils.equals(statusBarLight, "1")) {
                        binding!!.mainLayout.setPadding(0, ScreenUtils.getStatusBarHeight(), 0, 0)
                    }
                }
                return true;
            }
        }
        return false
    }

    private fun setStatusBarLight(url: String?) {
        if (!TextUtils.isEmpty(url)) {
            val uri = Uri.parse(url)
            val para = uri.queryParameterNames
            if (para != null && para.contains("wscnstatusbarlight")) {
                val statusBarLight = uri.getQueryParameter("wscnstatusbarlight")
                if (TextUtils.equals(statusBarLight, "1")) {
                    WindowHelper.changeStatusBarColor(this, 1.toFloat())
                    return
                }
            }
        }
        WindowHelper.changeStatusBarColor(this, 1.toFloat())
    }

    override fun onBackPressed() {
        if (mWeb.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }


    override fun isSupportSwipeBack(): Boolean {
        return false
    }
    open fun checkIsShowShareBtn(isShowShare: Boolean) {
        if (isShowShare) {
           binding!!.titleBar.setRightBtn2Text(ResourceUtils.getResStringFromId(com.wallstreetcn.baseui.R.string.icon_share))
           binding!!.titleBar.setRightBtn2OnclickListener {
                responseToShare()
            }
        } else {
            binding!!.titleBar.setRightBtn2Visible(View.INVISIBLE)
        }
    }

    private fun responseToShare() {
        if (mWeb.webView?.isLoadingFinish == true) {
            share()
        } else {
            mWeb.webView?.loadShareData()
            RxUtils.empty().delay(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { it.printStackTrace() }
                .doOnComplete {
                    share()
                }
                .subscribe()
        }
    }

    private fun share() {

    }

    private var binding : WebviewActivityWscnWebBinding? = null
    override fun doGetContentView(): View {
        if (binding == null)
            binding = WebviewActivityWscnWebBinding.inflate(layoutInflater)
        return binding!!.root
    }

}
package com.faqun;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

import com.faqun.lazyload.ImageLoadOkHttpClient;
import com.richzjc.observer.Observer;
import com.richzjc.observer.ObserverManger;
import com.wallstreetcn.helper.utils.TLog;
import com.wallstreetcn.helper.utils.UtilsContextManager;
import com.wallstreetcn.helper.utils.observer.ObserverIds;
import com.wallstreetcn.imageloader.ImageLoaderInit;

public class NewsApplication extends Application implements Thread.UncaughtExceptionHandler, Observer {

    @Override
    public void onCreate() {
        ObserverManger.getInstance().registerObserver(this, ObserverIds.AGREE_USER_PRIVACY);
        super.onCreate();
        UtilsContextManager.getInstance().init(this);
        ImageLoaderInit.init(this, new ImageLoadOkHttpClient().getOkHttpClient());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        onDestroy();
    }

    public void onDestroy() {
        ObserverManger.getInstance().removeObserver(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        TLog.e("WallApplication", "uncaughtException", e);

    }

    private Application getApplication() {
        return this;
    }



    @Override
    public void update(int id, Object... objects) {
        if(id == ObserverIds.AGREE_USER_PRIVACY){
        }
    }
}

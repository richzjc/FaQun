package com.wallstreetcn.helper.utils.timer;


import com.wallstreetcn.helper.utils.rx.RxUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by zhangyang on 16/7/20.
 */
public class TimerTaskHelper {
    private int DelayTime = 1;
    private List<Runnable> mRunnableList;
    private volatile static TimerTaskHelper instance;
    private TimerTaskHelper() {
        mRunnableList = Collections.synchronizedList(new ArrayList<Runnable>());
    }

    public static TimerTaskHelper getInstance() {
        if (instance == null) {
            synchronized (TimerTaskHelper.class) {
                if (instance == null) {
                    instance = new TimerTaskHelper();
                }
            }
        }
        return instance;
    }

    private Disposable intervalSub;

    public void start() {
        if (intervalSub == null) {
            intervalSub = RxUtils.interval(DelayTime, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    run();
                }

            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    throwable.printStackTrace();
                }
            });
        }
    }

    public void setRunnable(Runnable runnable) {
        if (!mRunnableList.contains(runnable)) {
            mRunnableList.add(runnable);
        }
    }

    public void removeRunnable(Runnable runnable) {
        if (mRunnableList.contains(runnable)) {
            mRunnableList.remove(runnable);
        }
    }


    private void run() {
        List<Runnable> runnableList = new ArrayList<>();
        runnableList.addAll(this.mRunnableList);
        for (Runnable run : runnableList) {
            try {
                run.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (intervalSub != null && !intervalSub.isDisposed()) {
            intervalSub.dispose();
            intervalSub = null;
        }
    }
}

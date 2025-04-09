package com.wallstreetcn.data.observable;

import android.database.Observable;

import com.wallstreetcn.helper.utils.rx.RxUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

/**
 * Created by wscn on 17/7/24.
 */

public class FinishOrDeleteObserverable extends Observable<IFinishOrDeleteCallback> {

    private static final FinishOrDeleteObserverable instance = new FinishOrDeleteObserverable();

    public static FinishOrDeleteObserverable getInstance() {
        return instance;
    }

    private FinishOrDeleteObserverable() {

    }

    public void notifyFinish(final Object obj, final String articleId, final String topiId) {
        RxUtils.empty()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        synchronized (mObservers) {
                            for (int i = mObservers.size() - 1; i >= 0; i--) {
                                mObservers.get(i).finish(obj, articleId, topiId);
                            }
                        }
                    }
                }).subscribe();
    }

    public void notifyDelete() {
        RxUtils.empty()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        synchronized (mObservers) {
                            for (int i = mObservers.size() - 1; i >= 0; i--) {
                                mObservers.get(i).delete();
                            }
                        }
                    }
                }).subscribe();
    }
}

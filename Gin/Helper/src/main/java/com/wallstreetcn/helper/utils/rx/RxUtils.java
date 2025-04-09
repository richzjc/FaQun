package com.wallstreetcn.helper.utils.rx;

import android.os.Build;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by chanlevel on 2017/2/9.
 */

public class RxUtils {

    public static void dispose(Disposable... subscriptions) {
        if (null != subscriptions)
            for (Disposable sp : subscriptions) {
                if (sp != null && !sp.isDisposed()) {
                    sp.dispose();
                }
            }
    }


    public static Observable<Long> delayDo(long interval, TimeUnit unit) {
        return Observable
                .interval(interval, unit)
                .take(1)
                .subscribeOn(RxUtils.getSchedulerIo())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Long> delayDo(int seconds) {
        return delayDo(seconds, TimeUnit.SECONDS);
    }

    public static Disposable delayDo(long interval, TimeUnit unit, Consumer<Long> action) {
        return delayDo(interval, unit)
                .subscribe(action);
    }

    public static Disposable delayDo(int seconds, Consumer<Long> action) {
        return delayDo(seconds).subscribe(action);
    }

    public static Observable<String> empty() {
        return Observable.just("");
    }

    public static <T> Observable<T> emptyT() {
        return Observable.empty();
    }

    public static <T> Observable<T> just(T item) {
        return Observable.just(item);
    }

    public static <T> Observable<T> from(T[] items) {
        return Observable.fromArray(items);
    }

    public static <T> Observable<T> fromIterable(Iterable<T> source) {
        return Observable.fromIterable(source);
    }

    public static Observable<Long> interval(long period, TimeUnit unit) {
        return Observable.interval(period, unit);
    }

    public static Observable<?> concat(Observable<?> observable0, Observable<?> observable1) {
        return Observable.concat(observable0, observable1);
    }

    public static Observable<?> concat(Observable<?> observable0, Observable<?> observable1, Observable<?> observable2) {
        return Observable.concat(observable0, observable1, observable2);
    }

    public static Observable<?> flatMap(Observable<?> observable0, Observable<?> observable1) {
        return Observable.merge(observable0, observable1);
    }

    public static Observable<?> flatMap(Observable<?> observable0, Observable<?> observable1, Observable<?> observable3) {
        return Observable.merge(observable0, observable1, observable3);
    }

    public static Scheduler getSchedulerIo() {
        if (Build.MANUFACTURER.toLowerCase().contains("huawei")) {
            return Schedulers.from(ThreadPoolShared.get());
        } else {
            return Schedulers.io();
        }
    }
}

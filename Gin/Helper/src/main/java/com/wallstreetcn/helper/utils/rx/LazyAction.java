package com.wallstreetcn.helper.utils.rx;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by chanlevel on 2016/11/15.
 */

public class LazyAction {
    private WrapperAction cs = new WrapperAction();
    private Disposable sp;

    public LazyAction(long milliseconds, Consumer<Integer> action1) {
        init(milliseconds, action1);
    }

    public LazyAction(Consumer<Integer> action1) {
        init(200, action1);
    }

    private void init(long milliseconds, Consumer<Integer> action1) {
        sp = Observable.create(cs).subscribeOn(Schedulers.newThread())
                .sample(milliseconds, TimeUnit.MILLISECONDS)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer != null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public void beep() {
        cs.beep();
    }

    public void dispose() {
        if (sp != null && !sp.isDisposed()) {
            sp.dispose();
        }
    }


    private class WrapperAction implements ObservableOnSubscribe<Integer> {

        ObservableEmitter<Integer> sb;
        private int cc = 0;

        void beep() {
            sb.onNext(++cc);
        }


        @Override
        public void subscribe(ObservableEmitter<Integer> e) throws Exception {

            this.sb = e;
        }
    }


}

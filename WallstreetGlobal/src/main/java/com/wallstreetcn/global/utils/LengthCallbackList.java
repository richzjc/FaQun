package com.wallstreetcn.global.utils;

import com.wallstreetcn.helper.utils.rx.RxUtils;

import java.util.ArrayList;
import java.util.Collection;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by wscn on 17/11/6.
 */

public class LengthCallbackList<E> extends ArrayList<E> {

    private final ArrayList<OnLengthChangeListener> mSelectedListeners = new ArrayList<>();

    @Override
    public boolean add(E o) {
        boolean flag = super.add(o);
        change();
        return flag;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean flag = super.addAll(c);
        change();
        return flag;
    }


    @Override
    public void add(int index, E element) {
        super.add(index, element);
        change();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean flag = super.addAll(index, c);
        change();
        return flag;
    }

    @Override
    public boolean remove(Object o) {
        boolean flag = super.remove(o);
        change();
        return flag;
    }

    @Override
    public E remove(int index) {
        E obj = super.remove(index);
        change();
        return obj;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        change();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean flag = super.removeAll(c);
        change();
        return flag;
    }

    @Override
    public void clear() {
        super.clear();
        change();
    }

    private void change() {
        RxUtils.empty()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(s -> {
                    for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
                        mSelectedListeners.get(i).onLengthChange();
                    }
                }).subscribe();
    }

    public void addOnLengthChanngeListener(@NonNull OnLengthChangeListener listener) {
        if (!mSelectedListeners.contains(listener)) {
            mSelectedListeners.add(listener);
        }
    }

    public void removeOnTabSelectedListener(@NonNull OnLengthChangeListener listener) {
        if (mSelectedListeners.contains(listener))
            mSelectedListeners.remove(listener);
    }

    public void clearOnTabSelectedListeners() {
        mSelectedListeners.clear();
    }


    public interface OnLengthChangeListener {
        void onLengthChange();
    }
}

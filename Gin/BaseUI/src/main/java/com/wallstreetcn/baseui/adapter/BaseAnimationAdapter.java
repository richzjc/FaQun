package com.wallstreetcn.baseui.adapter;

import android.animation.Animator;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.wallstreetcn.helper.utils.view.ViewHelper;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Leif Zhang on 2016/12/22.
 * Email leifzhanggithub@gmail.com
 */

public abstract class BaseAnimationAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private int mDuration = 300;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mLastPosition = -1;
    private boolean needAnimator = false;
    private boolean isFirstOnly = true;

    @Override
    public void onBindViewHolder(T holder, int position) {
        if (needAnimator) {
            int adapterPosition = holder.getAdapterPosition();
            if (!isFirstOnly || adapterPosition > mLastPosition) {
                for (Animator anim : getAnimators(holder.itemView)) {
                    anim.setDuration(mDuration).start();
                    anim.setInterpolator(mInterpolator);
                }
                mLastPosition = adapterPosition;
            } else {
                ViewHelper.clear(holder.itemView);
            }
        }
    }


    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setStartPosition(int start) {
        mLastPosition = start;
    }

    protected abstract Animator[] getAnimators(View view);

    public void setFirstOnly(boolean firstOnly) {
        isFirstOnly = firstOnly;
    }

    public void setNeedAnimator(boolean needAnimator) {
        this.needAnimator = needAnimator;
    }

}
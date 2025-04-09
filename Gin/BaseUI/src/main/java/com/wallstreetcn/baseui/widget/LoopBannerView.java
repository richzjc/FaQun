package com.wallstreetcn.baseui.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leif Zhang on 2016/11/24.
 * Email leifzhanggithub@gmail.com
 */

public class LoopBannerView extends RecyclerView {
    private LoopWrapAdapter<?> wrapAdapter;
    private String TAG = "LoopBannerView";
    private boolean canAutoScroll = true;

    private float originDownX;
    private float originDownY;
    private int touchSlop;
    private boolean isInit = false;
    private boolean isHorizontalScroll = false;

    public LoopBannerView(Context context) {
        this(context, null);
        init();
    }

    public LoopBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public LoopBannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnScrollListener(listener);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        setFocusable(false);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                originDownX = ev.getX();
                originDownY = ev.getY();
                isInit = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isInit) {
                    float dx = Math.abs(ev.getX() - originDownX);
                    float dy = Math.abs(ev.getY() - originDownY);
                    isInit = (dx > touchSlop) || (dy > touchSlop);
                    isHorizontalScroll = (dx > dy);
                }

                if (isInit) {
                    if (isHorizontalScroll) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        stopAutoScroll();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        stopAutoScroll();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                resetAutoScroll();
                break;
        }
        boolean result = false;
        try {
            result = super.onTouchEvent(e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return result;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(this);
        wrapAdapter = new LoopWrapAdapter<>(adapter);
        super.setAdapter(wrapAdapter);
    }


    public int getCurrentPosition() {
        int lastCompletelyVisibleItemPosition = 0;
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            lastCompletelyVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                    .findFirstCompletelyVisibleItemPosition();
            if (lastCompletelyVisibleItemPosition == NO_POSITION) {
                lastCompletelyVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            }
        }
        // TLog.i(TAG, String.valueOf(lastCompletelyVisibleItemPosition));
        return lastCompletelyVisibleItemPosition;
    }

    private Handler mAutoScrollHandler = new Handler();
    private int mAutoScrollTime = 3500;

    private Runnable mSmoothScroll = new Runnable() {
        @Override
        public void run() {
            if (getAdapter() == null)
                return;
            if (getAdapter().getItemCount() > 0) {
                int pos = getCurrentPosition();
                if (pos == NO_POSITION) {
                    scrollToPosition(wrapAdapter.getMiddlePosition());
                } else {
                    smoothScrollToPosition(pos + 1);
                }
            }
            resetAutoScroll();
        }
    };

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        //   TLog.i(TAG, String.valueOf(position));
    }


    public void resetAutoScroll() {
        if (canAutoScroll) {
            mAutoScrollHandler.removeCallbacks(mSmoothScroll);
            mAutoScrollHandler.postDelayed(mSmoothScroll, mAutoScrollTime);
        } else {
            stopAutoScroll();
        }
    }

    public void setAutoScrollTime(int time) {
        mAutoScrollTime = time;
    }

    public void stopAutoScroll() {
        mAutoScrollHandler.removeCallbacks(mSmoothScroll);
    }

    private List<OnPageChangeListener> pageChangeListeners;

    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (pageChangeListeners == null) {
            pageChangeListeners = new ArrayList<>();
        }
        if (!pageChangeListeners.contains(listener)) {
            pageChangeListeners.add(listener);
        }
    }

    private OnScrollListener listener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (pageChangeListeners != null) {
                    for (OnPageChangeListener listener : pageChangeListeners) {
                        listener.onPageSelected(wrapAdapter.getActualPosition(getCurrentPosition()));
                    }
                }
            }
        }
    };

    public interface OnPageChangeListener {
        void onPageSelected(int page);
    }

    public int getActualPosition(int position) {
        int actualPosition = wrapAdapter.getMiddlePosition();
        if (position >= wrapAdapter.getActualItemCount()) {
            actualPosition = position % wrapAdapter.getActualItemCount();
        }
        return actualPosition;
    }

    public void setCanAutoScroll(boolean canAutoScroll) {
        this.canAutoScroll = canAutoScroll;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        resetAutoScroll();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoScroll();
    }
}

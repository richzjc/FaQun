package com.wallstreetcn.global.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.wallstreetcn.helper.utils.system.ScreenUtils;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Leif Zhang on 2016/12/28.
 * Email leifzhanggithub@gmail.com
 */

public class RecyclerHeaderTouchListener implements RecyclerView.OnItemTouchListener {
    private StickyRecyclerHeadersAdapter adapter;

    public RecyclerHeaderTouchListener(RecyclerView recyclerView, StickyRecyclerHeadersDecoration decor, StickyRecyclerHeadersAdapter adapter) {
        this.mTapDetector = new GestureDetector(recyclerView.getContext(), new SingleTapDetector());
        this.mRecyclerView = recyclerView;
        this.mDecor = decor;
        this.adapter = adapter;
    }

    public StickyRecyclerHeadersAdapter getAdapter() {
        return adapter;
    }

    private final GestureDetector mTapDetector;
    private final RecyclerView mRecyclerView;
    private final StickyRecyclerHeadersDecoration mDecor;
    private RecyclerHeaderTouchListener.OnHeaderClickListener mOnHeaderClickListener;

  /*  public RecyclerHeaderTouchListener(RecyclerView recyclerView, StickyRecyclerHeadersDecoration decor) {
        this.mTapDetector = new GestureDetector(recyclerView.getContext(), new RecyclerHeaderTouchListener.SingleTapDetector());
        this.mRecyclerView = recyclerView;
        this.mDecor = decor;
    }*/


    public void setOnHeaderClickListener(RecyclerHeaderTouchListener.OnHeaderClickListener listener) {
        this.mOnHeaderClickListener = listener;
    }

    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        if (this.mOnHeaderClickListener != null) {
            boolean tapDetectorResponse = this.mTapDetector.onTouchEvent(e);
            if (tapDetectorResponse) {
                return true;
            }
            if (e.getAction() == 0) {
                int x = (int) e.getX();
                int y = (int) e.getY();
                int position = this.mDecor.findHeaderPositionUnder(x, y);
                if (position != -1) {
                    position = this.mDecor.findHeaderPositionUnder(x + ScreenUtils.dip2px(100), y + 100);
                }
                return position != -1;
            }
        }
        return false;
    }

    public void onTouchEvent(RecyclerView view, MotionEvent e) {
        if (this.mOnHeaderClickListener != null) {
            this.mTapDetector.onTouchEvent(e);
        }

    }


    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private class SingleTapDetector extends GestureDetector.SimpleOnGestureListener {
        private SingleTapDetector() {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            int position = RecyclerHeaderTouchListener.this.mDecor.findHeaderPositionUnder((int) e.getX(), (int) e.getY());
            if (position != -1) {
                View headerView = RecyclerHeaderTouchListener.this.mDecor.getHeaderView(RecyclerHeaderTouchListener.this.mRecyclerView, position);
                long headerId = RecyclerHeaderTouchListener.this.getAdapter().getHeaderId(position);
                RecyclerHeaderTouchListener.this.mOnHeaderClickListener.onHeaderClick(headerView, position, headerId);
                RecyclerHeaderTouchListener.this.mRecyclerView.playSoundEffect(0);
                headerView.onTouchEvent(e);
                return true;
            } else {
                return false;
            }
        }

        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(View var1, int var2, long var3);
    }
}
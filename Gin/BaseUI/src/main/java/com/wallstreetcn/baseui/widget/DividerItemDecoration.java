package com.wallstreetcn.baseui.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by zhangyang on 16/2/2.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int mOrientation;
    private int height = 2;
    private int margin = 0;
    private boolean drawLastLine = true;

    public DividerItemDecoration(int orientation, int color) {
        this(orientation, 2, color, 0);
    }

    public DividerItemDecoration(int orientation, int height, int color, int margin) {
        ColorDrawable drawable = new ColorDrawable();
        drawable.setColor(color);
        mDivider = drawable;
        setOrientation(orientation);
        this.height = height;
        this.margin = margin;
    }

    public DividerItemDecoration(int orientation, int height, int color, int margin, boolean needDrawLastLine) {
        ColorDrawable drawable = new ColorDrawable();
        drawable.setColor(color);
        mDivider = drawable;
        setOrientation(orientation);
        this.height = height;
        this.margin = margin;
        this.drawLastLine = needDrawLastLine;
    }


    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + margin;
        final int right = parent.getWidth() - parent.getPaddingRight() - margin;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + height;
            mDivider.setBounds(left, top, right, bottom);
            if(i == childCount - 1){
               if(drawLastLine)
                   mDivider.draw(c);
            }else {
                mDivider.draw(c);
            }
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop() + margin;
        final int bottom = parent.getHeight() - parent.getPaddingBottom() - margin;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + height;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, height);
        } else {
            outRect.set(0, 0, height, 0);
        }
    }
}
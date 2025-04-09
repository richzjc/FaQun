package com.wallstreetcn.baseui.widget.endless;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * Created by cundong on 2015/10/23.
 * <p/>
 * RecyclerView为GridLayoutManager时，设置了HeaderView，就会用到这个SpanSizeLookup
 */
public class HeaderSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private EndlessRecyclerAdapter adapter;
    private int mSpanSize = 1;

    public HeaderSpanSizeLookup(int spanSize) {
        this.mSpanSize = spanSize;
    }

    public void setAdapter(EndlessRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getSpanSize(int position) {
        if (!adapter.isEmptyView()) {
            return mSpanSize;
        } else {
            boolean isHeaderOrFooter = (adapter.getHeadCount() > 0 && position >= adapter.getHeadCount()) ||
                    position >= (adapter.getWrappedItemCount() + adapter.getHeadCount());

            return isHeaderOrFooter ? mSpanSize : 1;
        }
    }
}
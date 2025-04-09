package com.wallstreetcn.baseui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tubb.smrv.SwipeHorizontalMenuLayout;
import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.internal.ViewQuery;
import com.wallstreetcn.baseui.widget.CustomTextView;
import com.wallstreetcn.baseui.widget.RecyclerViewLayout;

/**
 * Created by Leif Zhang on 2017/2/21.
 * Email leifzhanggithub@gmail.com
 */

public abstract class BaseSwipeViewHolder<T> extends BaseRecycleViewHolder<T> {
    public View smMenuViewRight;
    public RecyclerViewLayout smContentView;
    public SwipeHorizontalMenuLayout horizontalMenuLayout;
    public CustomTextView delete;

    public BaseSwipeViewHolder(Context context) {
        super(LayoutInflater.from(context)
                .inflate(R.layout.base_recycler_item_swipe, null));
        mContext = context;
        if (mContext == null)
            mContext = itemView.getContext();
        horizontalMenuLayout = (SwipeHorizontalMenuLayout) itemView;
        smContentView = itemView.findViewById(com.tubb.smrv.R.id.smContentView);
        delete = itemView.findViewById(R.id.delete);
        itemContainer = smContentView;
        smMenuViewRight = itemView.findViewById(com.tubb.smrv.R.id.smMenuViewRight);
        View view = getHolderView(LayoutInflater.from(context), itemContainer);
        if (view != null && smContentView.getChildCount() == 0) {
            actualItemView = view;
            smContentView.addView(actualItemView);
        }


        ViewGroup.LayoutParams params = actualItemView.getLayoutParams();
        ViewGroup.LayoutParams itemParams = new ViewGroup.LayoutParams(params.width,
                params.height);
        itemView.setLayoutParams(itemParams);
        mViewQuery = new ViewQuery();
        mViewQuery.setView(itemView);
        doBindView(itemView);
    }


    @Override
    protected void doBindView(View itemView) {

    }
}

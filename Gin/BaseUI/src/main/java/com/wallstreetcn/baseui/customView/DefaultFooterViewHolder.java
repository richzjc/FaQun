package com.wallstreetcn.baseui.customView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.callback.IViewHolder;
import com.wallstreetcn.baseui.callback.IViewLoadAdapter;
import com.wallstreetcn.helper.utils.ResourceUtils;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;


/**
 * Created by Leif Zhang on 2017/3/30.
 * Email leifzhanggithub@gmail.com
 */

public class DefaultFooterViewHolder implements IViewHolder, IViewLoadAdapter{
    private View footerView;
    private TextView loadFinish;
    private View loadingParent;
    public static final int FOOTER_TAG = 10101;

    public DefaultFooterViewHolder(ViewGroup container) {
        footerView = LayoutInflater.from(container.getContext())
                .inflate(R.layout.base_load_more, container, false);
        footerView.setLayoutParams(new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        loadingParent = footerView.findViewById(R.id.loading_parent);
        loadFinish = footerView.findViewById(R.id.load_finish);
        loadFinish.setEnabled(false);
    }

    public void isRecyclerEnd(boolean isEnd) {
        if (!isEnd) {
            loadingParent.setVisibility(View.GONE);
            loadFinish.setVisibility(View.VISIBLE);
            loadFinish.setEnabled(false);
            loadFinish.setText(ResourceUtils.getResStringFromId(R.string.base_all_load_completed));
        } else {
            loadingParent.setVisibility(View.GONE);
            loadFinish.setVisibility(View.VISIBLE);
            loadFinish.setEnabled(true);
            loadFinish.setText(ResourceUtils.getResStringFromId(R.string.base_click_and_load_more));
        }
    }

    public void showLoading(){
        loadingParent.setVisibility(View.VISIBLE);
        loadFinish.setVisibility(View.GONE);
        loadFinish.setEnabled(false);
    }

    @Override
    public View getView() {
        footerView.setTag(FOOTER_TAG);
        return footerView;
    }

    public void setOnclickListener(View.OnClickListener listener){
        if(listener != null)
            loadFinish.setOnClickListener(listener);
    }
}

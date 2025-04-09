package com.wallstreetcn.baseui.customView;

import android.text.TextUtils;
import com.wallstreetcn.helper.utils.TLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.callback.IViewHolder;
import com.wallstreetcn.helper.utils.system.ScreenUtils;
import com.wallstreetcn.imageloader.ImageLoadManager;
import com.wallstreetcn.imageloader.WscnImageView;

/**
 * Created by Leif Zhang on 16/9/20.
 * Email leifzhanggithub@gmail.com
 */
public class DefaultEmptyViewHolder implements IViewHolder {

    private View view;
    private TextView tvEmpty;
    private TextView tvBtn;
    private ImageView emptyIv;

    public DefaultEmptyViewHolder(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.load_empty, parent, false);
        tvEmpty = view.findViewById(R.id.tv_empty);
        tvBtn = view.findViewById(R.id.tv_add_wits);
        emptyIv = view.findViewById(R.id.img_empty);
        setImageIvParams();
        emptyIv.setImageResource(com.wallstreetcn.baseui.R.drawable.msg_empty);
    }

    private void setImageIvParams() {
        int width = ScreenUtils.getScreenWidth();
        int ivWidth = width / 3;
        ViewGroup.LayoutParams params = emptyIv.getLayoutParams();
        params.width = ivWidth;
        params.height = ivWidth;
        emptyIv.setLayoutParams(params);
    }

    public void setEmptyText(String text) {
        if (TextUtils.isEmpty(text)) {
            tvEmpty.setVisibility(View.GONE);
        } else {
            tvEmpty.setText(text);
            tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    public void setTvBtn(int visible, String btnText, View.OnClickListener listener) {
        tvBtn.setVisibility(visible);
        tvBtn.setText(btnText);
        if (listener != null)
            tvBtn.setOnClickListener(listener);
    }

    public void setEmptyImageRes(int emptyImageRes) {
        emptyIv.setVisibility(emptyImageRes == 0 ? View.GONE : View.VISIBLE);
        emptyIv.setImageResource(emptyImageRes);
    }

    @Override
    public View getView() {
        return view;
    }

    public void setWrapContent() {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        view.setLayoutParams(params);
    }
}

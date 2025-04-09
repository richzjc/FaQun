package com.wallstreetcn.baseui.customView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.imageloader.ImageLoadManager;
import com.wallstreetcn.imageloader.WscnImageView;

/**
 * Created by wscn on 17/3/20.
 */

public class DefaultNetworkErrorViewHolder {

    private View parent;
    private WscnImageView imageView;
    private TextView desc1;
    private TextView desc2;
    private TextView emptyDesc;

    public DefaultNetworkErrorViewHolder(ViewGroup container) {
        parent = LayoutInflater.from(container.getContext())
                .inflate(R.layout.base_network_error_view, container, false);
        desc1 = parent.findViewById(R.id.desc1);
        desc2 = parent.findViewById(R.id.desc2);
        imageView = parent.findViewById(R.id.image);
        emptyDesc = parent.findViewById(R.id.emptyDesc);
    }

    public View getNetWorkErrorView() {
        desc1.setText(ResourceUtils.getResStringFromId(R.string.base_no_network_and_click_agian));
        desc2.setText(ResourceUtils.getResStringFromId(R.string.base_click_and_reload));
        ImageLoadManager.loadImage(R.drawable.network_error, imageView, 0);
        return parent;
    }

    public View get404ErrorView() {
        desc1.setText("");
        desc2.setText("");
        ImageLoadManager.loadImage(R.drawable.error_404, imageView, 0);
        emptyDesc.setText("404");
        return parent;
    }
}

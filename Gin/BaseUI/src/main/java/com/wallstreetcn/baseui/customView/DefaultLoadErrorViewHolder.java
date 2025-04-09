package com.wallstreetcn.baseui.customView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wallstreetcn.baseui.R;
import com.wallstreetcn.baseui.callback.IViewHolder;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.imageloader.ImageLoadManager;
import com.wallstreetcn.imageloader.WscnImageView;

/**
 * Created by wscn on 17/3/21.
 */

public class DefaultLoadErrorViewHolder implements IViewHolder {
    View parent;
    WscnImageView imageView;
    TextView desc1;
    TextView desc2;
    public DefaultLoadErrorViewHolder(ViewGroup container) {
        parent = LayoutInflater.from(container.getContext())
                .inflate(R.layout.base_network_error_view, container, false);
        desc1 = parent.findViewById(R.id.desc1);
        desc2 = parent.findViewById(R.id.desc2);
        imageView = parent.findViewById(R.id.image);
        desc1.setText("OOPS!");
        desc2.setText(ResourceUtils.getResStringFromId(R.string.base_trump_says_can_t_into));
        ImageLoadManager.loadImage(R.drawable.load_error, imageView, 0);
    }


    @Override
    public View getView() {
        return parent;
    }
}

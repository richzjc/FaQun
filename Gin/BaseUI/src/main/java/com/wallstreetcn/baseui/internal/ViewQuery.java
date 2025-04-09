package com.wallstreetcn.baseui.internal;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wallstreetcn.imageloader.ImageLoadManager;
import com.wallstreetcn.imageloader.WscnImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by micker on 16/6/16.
 */
public class ViewQuery {
    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;
    private Activity activity;
    private View view;
    private SparseArray views;

    public ViewQuery setActivity(Activity activity) {
        this.activity = activity;
        views = new SparseArray();
        return this;
    }

    public ViewQuery setView(View view) {
        this.view = view;
        views = new SparseArray();
        return this;
    }

    public View getRootView() {
        if (null != view) {
            return view;
        }
        if (null != activity) {
            return activity.findViewById(android.R.id.content);
        }
        return null;
    }

    public ViewQuery clearViews() {
        if (null != views) {
            views.clear();
        }
        return this;
    }

    public ViewQuery setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public ViewQuery setClickListener(int id, View.OnClickListener clickListener) {
        View view = findViewById(id);
        view.setOnClickListener(clickListener);
        return this;
    }

    public ViewQuery setLongClickListener(View.OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
        return this;
    }

    public ViewQuery addClickListener(View... args) {
        for (int i = 0; i < args.length; i++) {
            ((View) args[i]).setOnClickListener(this.clickListener);
        }
        return this;
    }

    public ViewQuery addClickListener(int... ids) {
        for (int i = 0; i < ids.length; i++) {
            View view = findViewById(ids[i]);
            if (null != view)
                (view).setOnClickListener(this.clickListener);
        }
        return this;
    }

    public ViewQuery setText(int viewId, CharSequence value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }


    public ViewQuery addLongClickListner(View... args) {
        for (int i = 0; i < args.length; i++) {
            ((View) args[i]).setOnLongClickListener(this.longClickListener);
        }
        return this;
    }

    public ViewQuery setBackgroundResId(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public void loadCircleImage(String url, int id, int defaultImage, int size) {
        WscnImageView imageView = findViewById(id);
        ImageLoadManager.loadCircleImage(url, imageView, defaultImage, size);
    }

    public ViewQuery addLongClickListner(int... ids) {
        for (int i = 0; i < ids.length; i++) {
            View view = findViewById(ids[i]);
            if (null != view)
                (view).setOnLongClickListener(this.longClickListener);
        }
        return this;
    }

    @Nullable
    public <T extends View> T findViewById(int id) {
        View view = null;
        try {
            view = (View) views.get(id);
            if (null == view) {
                view = ((null != this.activity) ? this.activity.findViewById(id) : this.view.findViewById(id));
                if (null != view) {
                    views.put(id, view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) view;
    }

    @Nullable
    public <T extends View> T findViewByTag(Object tag) {
        View view = null;
        try {
            if (null == this.activity)
                return null;
            view = ((null != this.activity) ? null : this.view.findViewWithTag(tag));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) view;
    }


    public ViewQuery setText(int id, String text) {
        TextView textView = findViewById(id);
        if (null != textView)
            textView.setText(text);
        return this;
    }


    public ViewQuery setText(int id, int resId) {
        TextView textView = findViewById(id);
        if (null != textView)
            textView.setText(resId);
        return this;
    }

    public ViewQuery setTextColor(int id, int resId) {
        TextView textView = findViewById(id);
        if (null != textView)
            textView.setTextColor(ContextCompat.getColor(textView.getContext(), resId));
        return this;
    }

    public ViewQuery setTextColor(int id, String color) {
        TextView textView = findViewById(id);
        if (null != textView)
            textView.setTextColor(Color.parseColor(color));
        return this;
    }

    public ViewQuery setTextSize(int id, float size) {
        TextView textView = findViewById(id);
        if (null != textView)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    public ViewQuery setImageDrawable(int id, int resId) {
        ImageView imageView = findViewById(id);
        if (null != imageView)
            imageView.setImageDrawable(ContextCompat.getDrawable(getRootView().getContext(), resId));
        return this;
    }

    public ViewQuery setImageDrawable(int id, Drawable resId) {
        ImageView imageView = findViewById(id);
        if (null != imageView)
            imageView.setImageDrawable(resId);
        return this;
    }

    public ViewQuery setVisible(int id, int visibility) {
        View view = findViewById(id);
        if (null != view)
            view.setVisibility(visibility);
        return this;
    }

    public ViewQuery setAlpha(int id, float alpha) {
        View view = findViewById(id);
        if (null != view)
            view.setAlpha(alpha);
        return this;
    }

    public ViewQuery setBackgroundResource(int id, int resId) {
        View view = findViewById(id);
        if (null == view)
            return this;
        if (view instanceof ImageView)
            ((ImageView) view).setImageResource(resId);
        if (null != view)
            view.setBackgroundResource(resId);
        return this;
    }

    public ViewQuery setBackgroundDrawable(int id, int resId) {
        View view = findViewById(id);
        if (view instanceof ImageView)
            ((ImageView) view).setImageResource(resId);
        if (null != view) {
            view.setBackground(ContextCompat.getDrawable(getRootView().getContext(), resId));
        }
        return this;
    }

    public ViewQuery setBackgroundColor(int id, int resId) {
        View view = findViewById(id);
        if (null != view)
            view.setBackgroundColor(resId);
        return this;
    }

    public ViewQuery setBackgroundColor(int id, String color) {
        View view = findViewById(id);
        if (null != view)
            view.setBackgroundColor(Color.parseColor(color));
        return this;
    }

    public ViewQuery setEnable(int id, boolean enable) {
        View view = findViewById(id);
        if (null != view)
            view.setEnabled(enable);
        return this;
    }

    public TextView asTextView(int id) {
        return (TextView) findViewById(id);
    }

    public ImageView asImageView(int id) {
        return (ImageView) findViewById(id);
    }

    public Button asButton(int id) {
        return (Button) findViewById(id);
    }

    public EditText asEditText(int id) {
        return (EditText) findViewById(id);
    }

    public LinearLayout asLinearLayout(int id) {
        return (LinearLayout) findViewById(id);
    }

    public RelativeLayout asRelativeLayout(int id) {
        return (RelativeLayout) findViewById(id);
    }

    public WebView asWebView(int id) {
        return (WebView) findViewById(id);
    }

    public ViewPager asViewPager(int id) {
        return (ViewPager) findViewById(id);
    }

}

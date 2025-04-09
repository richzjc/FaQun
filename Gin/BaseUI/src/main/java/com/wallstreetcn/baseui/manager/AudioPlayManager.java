package com.wallstreetcn.baseui.manager;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * Created by ichongliang on 16/03/2018.
 */

public class AudioPlayManager {

    /**
     * 替换页面的contentView, setContentView(View)
     *
     * @param v
     * @return
     */
    public static View addPlayView(View v) {
        List<View> children = getAllChildViews(v);
        for (View view : children) {
            if (view instanceof CoordinatorLayout) {
                CoordinatorLayout layout = (CoordinatorLayout) view;
                int last = layout.getChildCount() - 1;
                last = last < 0 ? 0 : last;
                View lastView = layout.getChildAt(last);
                setCoordinatorLayout((CoordinatorLayout) view, lastView);
                return v;
            }
        }

        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeView(v);
        }
        CoordinatorLayout layout = new CoordinatorLayout(v.getContext());
        layout.addView(v);
        setCoordinatorLayout(layout, v);
        return layout;
    }

    /**
     * 页面存在 CoordinatorLayout 控件时调用
     *
     * @param coord
     */
    public static void setCoordinatorLayout(CoordinatorLayout coord) {
        View lastView = coord.getChildAt(coord.getChildCount() - 1);
        setCoordinatorLayout(coord, lastView);
    }

    private static void setCoordinatorLayout(CoordinatorLayout coord, View lastView) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) lastView.getLayoutParams();
        if (params.getBehavior() == null)
            params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        try {
            View obj = getAudioPlayView(coord.getContext());
            if (obj != null) {
                coord.addView(obj);
                CoordinatorLayout.LayoutParams objParams = (CoordinatorLayout.LayoutParams) obj.getLayoutParams();
                objParams.gravity = Gravity.BOTTOM;
                objParams.anchorGravity = Gravity.BOTTOM;
                obj.setLayoutParams(objParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static View getAudioPlayView(Context context) {
        try {
            Class c = Class.forName("com.wallstreetcn.podcast.widget.AudioPlayBottomView");
            Constructor con = c.getConstructor(Context.class);
            View obj = (View) con.newInstance(context);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                allchildren.add(viewchild);
                //再次 调用本身（递归）
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        return allchildren;
    }


}

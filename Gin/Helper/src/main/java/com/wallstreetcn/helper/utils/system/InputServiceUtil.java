package com.wallstreetcn.helper.utils.system;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Sky on 2016/5/23.
 */
public class InputServiceUtil {

    /*
     * 控制软键盘打开或者收起的工具类
     * */
    public static void showSoftInput(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);//强制显示
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftInput(View view) {
//        view为接受软键盘输入的视图
        try {
            if (view == null)
                return;
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isSoftInputActive(Context ctxt) {
        try {
            InputMethodManager imm = (InputMethodManager) ctxt.getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.isActive();//isOpen若返回true，则表示输入法打开
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}

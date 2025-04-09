package com.wallstreetcn.baseui.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.wallstreetcn.baseui.base.BaseActivity;
import com.wallstreetcn.helper.utils.Util;
import com.wallstreetcn.helper.utils.UtilsContextManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by wscn on 15/6/26.
 */
public class AppManager {

    private Stack<AppCompatActivity> activityStack;
    private volatile static AppManager instance;

    private AppManager() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            synchronized (AppManager.class) {
                if (instance == null) {
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(AppCompatActivity activity) {
        if (!activityStack.contains(activity)) {
            activityStack.add(activity);
        } else {
            activityStack.remove(activity);
            activityStack.add(activity);
        }
        UtilsContextManager.getInstance().setActivity(activity);

        if (TextUtils.equals("MainActivity", activity.getClass().getSimpleName())) {
            UtilsContextManager.getInstance().setMainActivity(activity);
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityStack.empty()) {
            return null;
        }
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 判断是否为最上层的Activity
     */
    public boolean isTopActivity(Activity activity) {
        try {
            return !activityStack.empty() && activity == activityStack.lastElement();
        } catch (Exception e) {
            return false;
        }
    }

    public AppCompatActivity getTopActivity() {
        if(activityStack.size() > 0){
           AppCompatActivity activity = activityStack.lastElement();
           if(activity == null || activity.isFinishing() || activity.isDestroyed()){
               removeActivity(activity);
               return getTopActivity();
           }else{
               return activity;
           }
        }else{
            return null;
        }

    }

    public AppCompatActivity getMainActivity() {
        Iterator<AppCompatActivity> iterator = activityStack.iterator();
        AppCompatActivity activity = null;
        while (iterator.hasNext()) {
            AppCompatActivity compatActivity = iterator.next();
            if (TextUtils.equals("MainActivity", compatActivity.getClass().getSimpleName())) {
                activity = compatActivity;
                break;
            }
        }
        return activity;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activityStack != null) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

    public void removeActivity(Activity activity) {
        if (activityStack != null) {
            if (activity != null) {
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                    break;
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack == null)
            return;
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     */
    public Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isActivityAlive(Class<?> cls) {
        if (activityStack != null) {
            List<AppCompatActivity> list = new ArrayList<>(activityStack);
            for (Activity activity : list) {
                if (activity.getClass().equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSingleActivity() {
        if (activityStack != null && activityStack.size() == 1)
            return true;
        else
            return false;
    }

    public void recreateAllActivity() {
        Stack<Activity> newStack = new Stack<>();
        newStack.addAll(activityStack);
        for (Activity activity : newStack) {
            activity.recreate();
        }
    }

    public void updateAllConfiguration(int mode) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity instanceof BaseActivity) {
                    ((BaseActivity) activity).toggleNightMode(mode);
                }
            }
        }
    }
}

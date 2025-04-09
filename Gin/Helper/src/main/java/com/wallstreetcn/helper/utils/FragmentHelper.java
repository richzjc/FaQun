package com.wallstreetcn.helper.utils;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.wallstreetcn.helper.R;
import com.wallstreetcn.helper.utils.router.DoubleClickHelper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by zhangyang on 16/1/28.
 */
public class FragmentHelper {

    public static void addFragment(FragmentManager fm, int id, Fragment fragment, boolean ani, String tag) {
        if (fragment.isAdded()) {
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        if (ani) {
            ft.setCustomAnimations(R.anim.fragment_in, 0);
        }
        if (!TextUtils.isEmpty(tag))
            ft.add(id, fragment, tag);
        else
            ft.add(id, fragment);
        ft.commitAllowingStateLoss();
    }


    public static void addFragment(FragmentManager fm, int id, Fragment fragment) {
        addFragment(fm, id, fragment, false);
    }

    public static void addFragment(FragmentManager fm, int id, Fragment fragment, boolean ani) {
        if (fragment.isAdded()) {
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        if (ani) {
            ft.setCustomAnimations(R.anim.fragment_in, 0);
        }
        ft.add(id, fragment);
        ft.commitAllowingStateLoss();
    }

    public static void addFragment(FragmentManager fm, int id, Fragment... fragments) {
        for (Fragment fragment : fragments) {
            addFragment(fm, id, fragment, false);
        }
    }

    public static void removeFragment(FragmentManager fm, Fragment fragment) {
        removeFragment(fm, fragment, false);
    }

    public static void removeFragment(FragmentManager fm, Fragment fragment, boolean ani) {
        if (!fragment.isAdded()) {
            return;
        }
        FragmentTransaction ft = fm.beginTransaction();
        if (ani) {
            ft.setCustomAnimations(0, R.anim.fragment_out);
        }
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    public static void replaceFragment(FragmentManager fm, int id, Fragment fragment) {
        if (fragment.isAdded())
            return;
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment);
        ft.commitAllowingStateLoss();
    }

    public static void startActivityForResult(Fragment fragment, Class targetActivity, int result, Bundle bundle) {
        if (!DoubleClickHelper.doubleClickCheck())
            return;
        Intent intent = new Intent();
        intent.setClass(fragment.getActivity(), targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, fragment, result);
    }


    public static void startActivityForResult(Intent intent, Fragment fragment, int resultCode) {
        fragment.startActivityForResult(intent, resultCode);
    }


}

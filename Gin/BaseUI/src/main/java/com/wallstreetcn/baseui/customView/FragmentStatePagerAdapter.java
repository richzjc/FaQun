package com.wallstreetcn.baseui.customView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

/**
 * Created by Leif Zhang on 2016/10/16.
 * Email leifzhanggithub@gmail.com
 */
public abstract class FragmentStatePagerAdapter extends androidx.fragment.app.FragmentStatePagerAdapter {

    public FragmentStatePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
}

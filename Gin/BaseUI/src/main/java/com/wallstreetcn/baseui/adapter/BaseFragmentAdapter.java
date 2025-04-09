package com.wallstreetcn.baseui.adapter;

import com.wallstreetcn.baseui.customView.FragmentStatePagerAdapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Created by micker on 16/6/20.
 */
public class BaseFragmentAdapter<T extends Fragment> extends FragmentStatePagerAdapter {

    private List<T> fragments;
    private List<String> titles;

    public BaseFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void configData(List<String> titles, List<T> fragments) {
        this.titles = titles;
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    public void configData(List<T> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments == null || position >= fragments.size())
            return null;
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return (null == fragments) ? 0 : fragments.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null)
            return titles.get(position);
        else
            return "";
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}

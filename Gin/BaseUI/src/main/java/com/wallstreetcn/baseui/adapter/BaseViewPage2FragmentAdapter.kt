package com.wallstreetcn.baseui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BaseViewPage2FragmentAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

    private var fragments: List<Fragment>? = null
    private var titles: List<String>? = null

    fun configData(titles: List<String>?, fragments: List<Fragment>?) {
        this.titles = titles
        this.fragments = fragments
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return if ((null == fragments)) 0 else fragments!!.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments!![position]
    }

    fun getTitle(position: Int) : String{
        return titles!!.get(position)
    }
}
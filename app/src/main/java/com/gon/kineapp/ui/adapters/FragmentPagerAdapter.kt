package com.gon.kineapp.ui.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

class FragmentPagerAdapter(fm: FragmentManager): CustomFragmentStatePagerAdapter(fm) {

    private val pages: MutableList<Fragment> = ArrayList()

    fun addPage(page: Fragment) {
        pages.add(page)
        notifyDataSetChanged()
    }

    override fun getItem(p0: Int): Fragment {
        return pages[p0]
    }

    override fun getCount(): Int {
        return pages.size
    }
}
package com.gon.kineapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentPagerAdapter(fm: androidx.fragment.app.FragmentManager): CustomFragmentStatePagerAdapter(fm) {

    private val pages: MutableList<androidx.fragment.app.Fragment> = ArrayList()

    fun addPage(page: androidx.fragment.app.Fragment) {
        pages.add(page)
        notifyDataSetChanged()
    }

    override fun getItem(p0: Int): androidx.fragment.app.Fragment {
        return pages[p0]
    }

    override fun getCount(): Int {
        return pages.size
    }
}
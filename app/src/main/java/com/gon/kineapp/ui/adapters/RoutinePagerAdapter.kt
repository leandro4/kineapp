package com.gon.kineapp.ui.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.gon.kineapp.model.Routine
import com.gon.kineapp.ui.fragments.ExercisesFragment

class RoutinePagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    val fragments = ArrayList<ExercisesFragment>()

    private val days = listOf(
        "Lun",
        "Mar",
        "Mié",
        "Jue",
        "Vie",
        "Sáb",
        "Dom"
    )

    fun addFragment(f: ExercisesFragment) {
        fragments.add(f)
    }

    override fun getItem(p0: Int): Fragment {
        return fragments[p0]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return days[position]
    }
}
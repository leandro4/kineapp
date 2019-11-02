package com.gon.kineapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.gon.kineapp.model.Routine
import com.gon.kineapp.ui.fragments.ExercisesFragment

class RoutinePagerAdapter(fm: androidx.fragment.app.FragmentManager): androidx.fragment.app.FragmentPagerAdapter(fm) {

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

    override fun getItem(p0: Int): androidx.fragment.app.Fragment {
        return fragments[p0]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return days[position]
    }
}
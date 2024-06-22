package com.example.moneymanager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = listOf(
        Harian(),
        Kalender(),
        Bulanan(),
        Total(),
        Catatan()
    )

    private val fragmentTitleList = listOf(
        "Harian",
        "Kalender",
        "Bulanan",
        "Total",
        "Catatan"
    )

    override fun getCount(): Int = fragmentList.size

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getPageTitle(position: Int): CharSequence = fragmentTitleList[position]
}

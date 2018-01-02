package com.recrutify.empty1.empty1

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by arturnowak on 01.01.2018.
 */
class CustomPagerAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {

    val mFragmentManager = fm
    val mFragmentItems:ArrayList<Fragment> = ArrayList()
    val mFragmentTitles:ArrayList<String> = ArrayList()

    fun addFragment(fragment:Fragment, title: String) {
        mFragmentItems.add(fragment)
        mFragmentTitles.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentItems[position]
    }

    override fun getCount(): Int {
        return mFragmentItems.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitles[position]
    }

}
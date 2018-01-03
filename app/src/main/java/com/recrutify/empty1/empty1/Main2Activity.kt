package com.recrutify.empty1.empty1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.recrutify.empty1.empty1.main2fragmants.CandidatesFragment
import com.recrutify.empty1.empty1.main2fragmants.ProjectsFragment
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    var pagerAdapter:CustomPagerAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //setSupportActionBar(toolbar_main)

        pagerAdapter = CustomPagerAdapter(this.supportFragmentManager)

        pagerAdapter!!.addFragment(CandidatesFragment(), "Candidates")
        pagerAdapter!!.addFragment(ProjectsFragment(), "Projects")


        custom_view_pager.adapter = pagerAdapter

        custom_tab_layout.setupWithViewPager(custom_view_pager)
    }
}

package com.recrutify.empty1.empty1.candidate

import com.recrutify.empty1.empty1.R

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_candidate.*
import kotlinx.android.synthetic.main.appbar_main.*

class CandidateActivity : AppCompatActivity() {

    var pagerAdapter:CandidatePagerAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)

        //setSupportActionBar(toolbar_main)

        pagerAdapter = CandidatePagerAdapter(this.supportFragmentManager)

        pagerAdapter!!.addFragment(InformationFragment(), "Informacje")
        pagerAdapter!!.addFragment(ProjectsFragment(), "Projekty")
        pagerAdapter!!.addFragment(CVFragment(), "CV")
        pagerAdapter!!.addFragment(ActivityFragment(), "Aktywności")

        candidate_view_pager.adapter = pagerAdapter

        candidate_tab_layout.setupWithViewPager(candidate_view_pager)
    }
}

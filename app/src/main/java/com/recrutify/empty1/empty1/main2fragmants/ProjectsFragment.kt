package com.recrutify.empty1.empty1.main2fragmants


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.recrutify.empty1.empty1.R


/**
 * A simple [Fragment] subclass.
 */
class ProjectsFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_projects, container, false)
    }

}// Required empty public constructor

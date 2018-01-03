package com.recrutify.empty1.empty1.main2fragmants


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.LayoutDirection
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.success
import com.recrutify.empty1.empty1.CandidateContainer
import com.recrutify.empty1.empty1.CandidatesCustomAdapter

import com.recrutify.empty1.empty1.R


/**
 * A simple [Fragment] subclass.
 */
class CandidatesFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_candidates, container, false)

        val candidatesList = view.findViewById<RecyclerView>(R.id.candidatesList)

        candidatesList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

         "/utils/checkNumber/503173127".httpGet()
                .responseObject(CandidateContainer.Deserializer()) { request, response, result ->

                    Log.i("RESULT", result.toString())
                    Log.i("REQUEST", request.toString())

                    result.success { s ->
                        Log.d("SUCCESS", s.toString())

                        val candidates = arrayListOf<CandidateContainer>(s, s, s, s, s, s, s, s, s, s, s, s, s, s,s,s,s,s,s,s,s,s,s,s,s,s,s, s, s, s, s, s, s, s, s, s, s, s, s, s,s,s,s,s,s,s,s,s,s,s,s,s)

                        candidatesList.adapter = CandidatesCustomAdapter(candidates)

                    }
                }


        return view
    }


//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//
//        //candidatesList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//
//        "/utils/checkNumber/503173127".httpGet()
//                .responseObject(CandidateContainer.Deserializer()) { request, response, result ->
//
//                    Log.i("RESULT", result.toString())
//                    Log.i("REQUEST", request.toString())
//
//                    result.success { s ->
//                        Log.d("SUCCESS", s.toString())
//
//                        val candidates = arrayListOf<CandidateContainer>(s, s, s, s, s, s, s, s, s, s, s, s, s, s,s,s,s,s,s,s,s,s,s,s,s,s)
//
//                        candidatesList.adapter = CandidatesCustomAdapter(candidates)
//
//                    }
//                }
//
//
//
//    }

}// Required empty public constructor

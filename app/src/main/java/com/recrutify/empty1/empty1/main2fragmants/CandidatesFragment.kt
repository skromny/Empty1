package com.recrutify.empty1.empty1.main2fragmants


import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
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
import android.widget.AbsListView
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.success
import com.recrutify.empty1.empty1.Candidate
import com.recrutify.empty1.empty1.CandidatesCustomAdapter

import com.recrutify.empty1.empty1.R


/**
 * A simple [Fragment] subclass.
 */
class CandidatesFragment : Fragment() {


    var isLoading: Boolean = false
    var isLastPage: Boolean = false

    val candidates: ArrayList<Candidate> = arrayListOf()
    var candidate: Candidate? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_candidates, container, false)

        val candidatesList = view.findViewById<RecyclerView>(R.id.candidatesList)

        candidatesList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        candidatesList.setOnScrollListener (object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView?.layoutManager as LinearLayoutManager

                val visibleItemCount = layoutManager.getChildCount()
                val totalItemCount = layoutManager.getItemCount()
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= 4) {
                        //anonimowa funkcja
                        "/candidate".httpGet(listOf(Pair("page", totalItemCount/25 +1)))
                                .responseObject(Candidate.ListDeserializer()) { request, response, result ->

                                    Log.i("RESULT", result.toString())
                                    Log.i("REQUEST", request.toString())

                                    result.success { s ->
                                        Log.d("SUCCESS", s.toString())

                                        candidates.addAll(s)
                                        candidatesList.adapter.notifyDataSetChanged()
                                    }
                                }


                    }
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }


        })

        "/candidate".httpGet(listOf(Pair("page", "1")))
                .responseObject(Candidate.ListDeserializer()) { request, response, result ->

                    Log.i("RESULT", result.toString())
                    Log.i("REQUEST", request.toString())

                    result.success { s ->
                        Log.d("SUCCESS", s.toString())

                        candidates.addAll(s)
                        candidatesList.adapter = CandidatesCustomAdapter(candidates)
                    }
                }
//         "/candidate".httpGet(listOf(Pair("page", "1")))
//                .responseObject(CandidateContainer.Deserializer()) { request, response, result ->
//
//                    Log.i("RESULT", result.toString())
//                    Log.i("REQUEST", request.toString())
//
//                    result.success { s ->
//                        Log.d("SUCCESS", s.toString())
//
//                        candidate = s
//                        candidates.addAll(arrayListOf(s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s))
//
//                        candidatesList.adapter = CandidatesCustomAdapter(candidates)
//
//                    }
//                }


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

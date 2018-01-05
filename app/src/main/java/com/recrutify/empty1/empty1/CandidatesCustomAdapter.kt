package com.recrutify.empty1.empty1

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by arturnowak on 02.01.2018.
 */
class CandidatesCustomAdapter(val candidates:ArrayList<Candidate>) : RecyclerView.Adapter<CandidatesCustomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_candidate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if(holder?.avatarImage != null)
            Glide.with(holder?.avatarImage).load(candidates[position].avatarLink).into(holder?.avatarImage)

        holder?.candidateName?.text = candidates[position].name
        holder?.position?.text = candidates[position].position
        holder?.description?.text = candidates[position].description
    }

    override fun getItemCount(): Int {
        return candidates.size
    }


    class ViewHolder(view:View) : RecyclerView.ViewHolder(view) {

        val avatarImage = view.findViewById<CircleImageView>(R.id.avatarImage)
        val candidateName = view.findViewById<TextView>(R.id.candidateName)
        val position = view.findViewById<TextView>(R.id.c_position)
        val description = view.findViewById<TextView>(R.id.description)
    }
}
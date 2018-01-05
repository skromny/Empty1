package com.recrutify.empty1.empty1

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
        if(candidates[position].avatarLink != null)
            Glide.with(holder?.avatarImage)
                    .load(candidates[position].avatarLink)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            holder?.avatarImage?.setImageResource(R.drawable.ic_boss_1)
                            return true
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                    })
                    //.fallback(R.drawable.ic_boss_1)
                    .into(holder?.avatarImage)
        else
            holder?.avatarImage?.setImageResource(R.drawable.ic_boss_1)

        if (candidates[position].name!=null)
            holder?.candidateName?.text =  candidates[position].name
        if (candidates[position].position!=null)
            holder?.position?.text =  candidates[position].position
        if (candidates[position].description!=null)
            holder?.description?.text =  candidates[position].description
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
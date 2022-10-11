package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.NotificationModel
import com.squareup.picasso.Picasso

class NotificationAdapter(val notificationModel: MutableList<NotificationModel>): RecyclerView.Adapter<NotificationViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_notification,parent,false)
        return NotificationViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        return holder.bindView(notificationModel[position])
    }

    override fun getItemCount(): Int {
        return notificationModel.size
    }
}

class NotificationViewHolder(itemView: View, listener: NotificationAdapter.onItemClickListener): RecyclerView.ViewHolder(itemView){
//    private val tvName: TextView = itemView.findViewById(R.id.tvName)
//    private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
//    private val tvGenres: TextView = itemView.findViewById(R.id.tvGenres)
//    private val tvPlatforms: TextView = itemView.findViewById(R.id.tvPlatforms)
//    private val tvSummary: TextView = itemView.findViewById(R.id.tvSummary)
//    private val ivCover: ImageView = itemView.findViewById((R.id.ivCover))
    private val tvTitle: TextView = itemView.findViewById((R.id.notificationTitle))
    private val tvText: TextView = itemView.findViewById((R.id.notificationText))

    init{
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }

    }

    fun bindView(notificationModel: NotificationModel){
//        tvName.text = gameModel.name
//        val url = "https:"+gameModel.cover?.url.toString()
//        Picasso.get().load(url).placeholder(R.drawable.ic_baseline_image_not_supported_24).into(ivCover)
//        tvStatus.text = gameModel.status.toString()
//        val genreList:List<String?> = gameModel.genres.map { it?.name }
//        tvGenres.text = genreList.toString()
//        val platformList:List<String?> = gameModel.platforms.map { it?.name }
//        tvPlatforms.text = platformList.toString()
//        tvSummary.text = gameModel.summary

        when(notificationModel.type){
            "friendRequest" -> {
                tvTitle.text = "Friend Request"
                tvText.text = "${notificationModel.userUsername} wants to be your friend"
            }
            "gameRecommendation" -> {
                tvTitle.text = "Game Recommendation"
                tvText.text = "${notificationModel.userUsername} thinks you will like ${notificationModel.gameName}"
            }
        }

    }
}
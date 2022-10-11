package com.example.myapplication.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.FriendModel

class FriendAdapter(val friendModel: MutableList<FriendModel>): RecyclerView.Adapter<FriendViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_friend,parent,false)
        return FriendViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        return holder.bindView(friendModel[position])
    }

    override fun getItemCount(): Int {
        return friendModel.size
    }
}

class FriendViewHolder(itemView: View, listener: FriendAdapter.onItemClickListener): RecyclerView.ViewHolder(itemView){
    private val tvFriendName: TextView = itemView.findViewById(R.id.tvFriendName)

    init{
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }

    fun bindView(friendModel: FriendModel){
        tvFriendName.text = friendModel.username
    }
}
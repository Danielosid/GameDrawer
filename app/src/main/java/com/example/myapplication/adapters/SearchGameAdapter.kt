package com.example.myapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.InterestAdapter
import com.example.myapplication.models.GameModel
import com.squareup.picasso.Picasso

class SearchGameAdapter(val gameModel: MutableList<GameModel>): RecyclerView.Adapter<SearchGameViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_search_game,parent,false)
        return SearchGameViewHolder(view,mListener)
    }

    override fun onBindViewHolder(holder: SearchGameViewHolder, position: Int) {
        return holder.bindView(gameModel[position])
    }

    override fun getItemCount(): Int {
        return gameModel.size
    }
}

class SearchGameViewHolder(itemView: View,listener: SearchGameAdapter.onItemClickListener): RecyclerView.ViewHolder(itemView){
    private val tvName: TextView = itemView.findViewById(R.id.tvName)
    private val cardGameRecyclerView: RecyclerView = itemView.findViewById(R.id.cardGameRecyclerView)
    private val ivCover: ImageView = itemView.findViewById((R.id.ivCover))

    init{
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }

    fun bindView(gameModel: GameModel){
        tvName.text = gameModel.name
        val url = "https:"+gameModel.cover?.url.toString().replace("thumb", "cover_big")
        Picasso.get().load(url).placeholder(R.drawable.ic_baseline_image_not_supported_24).into(ivCover)
        val genreList:List<String?> = gameModel.genres.map { it?.name }
        cardGameRecyclerView.apply {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            val interestAdapter = InterestAdapter(genreList as ArrayList<String>)
            adapter = interestAdapter
        }
    }
}
package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class InterestAdapter(private val dataSet: ArrayList<String>): RecyclerView.Adapter<InterestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_interest,parent,false)
        return InterestViewHolder(view)
    }

    override fun onBindViewHolder(holder: InterestViewHolder, position: Int) {
        return holder.bindView(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}

class InterestViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val tvInterestName: TextView

    init{
        tvInterestName  = itemView.findViewById(R.id.tvInterestName)
    }

    fun bindView(interestName: String){
        tvInterestName.text = interestName
    }
}
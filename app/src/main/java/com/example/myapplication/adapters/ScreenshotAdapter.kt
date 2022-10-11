package com.example.myapplication.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.squareup.picasso.Picasso

class ScreenshotAdapter(private val dataSet: Array<String>): RecyclerView.Adapter<ScreenshotViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenshotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_screenshot,parent,false)
        return ScreenshotViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScreenshotViewHolder, position: Int) {
        return holder.bindView(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}

class ScreenshotViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val screenshotImage: ImageView

    init{
        screenshotImage  = itemView.findViewById(R.id.screenshotImage)
    }

    fun bindView(screenshotUrl: String){
        Picasso.get().load("https:$screenshotUrl").into(screenshotImage)
    }
}
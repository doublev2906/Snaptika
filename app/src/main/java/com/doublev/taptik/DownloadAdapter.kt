package com.doublev.taptik

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doublev.taptik.database.DownloadData

class DownloadAdapter(private val itemList: List<DownloadData>) : RecyclerView.Adapter<DownloadAdapter.ViewHolder>() {
    private lateinit var context : Context
  class ViewHolder(private val view:View) : RecyclerView.ViewHolder(view){
        val title:TextView = view.findViewById(R.id.tv_title)
        val author:TextView = view.findViewById(R.id.tvAuthor)
        val thumbnail:ImageView = view.findViewById(R.id.thumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.download_item,parent,false)
        context = parent.context
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemList[position]
        Glide.with(context).load(data.thumbnailUrl).into(holder.thumbnail)
        data.thumbnailUrl?.let { Log.e("abc", it) }
        holder.title.text = data.title
        holder.author.text = data.author
    }

    override fun getItemCount(): Int = itemList.size
}
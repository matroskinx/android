package com.example.vladislav.android5.Models

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vladislav.android5.Interfaces.ItemClickListener

import com.example.vladislav.android5.R


class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
    var textTitle: TextView
    var imageView: ImageView

    private var itemClickListener : ItemClickListener?=null

    init {
        textTitle = itemView.findViewById(R.id.cardTextTitle)
        imageView = itemView.findViewById(R.id.imageView)

        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener
    }
    override fun onClick(v: View?) {
        itemClickListener!!.onClick(v as View, adapterPosition, false)
    }

    override fun onLongClick(v: View?): Boolean {
        itemClickListener!!.onClick(v as View, adapterPosition, true)
        return true
    }
}


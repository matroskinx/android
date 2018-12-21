package com.example.vladislav.android5.Models

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vladislav.android5.Interfaces.ItemClickListener
import com.example.vladislav.android5.R
import com.example.vladislav.android5.WebViewActivity
import com.squareup.picasso.Picasso

class FeedAdapter(private val rssObject: RSSObject, private val mContext: Context): RecyclerView.Adapter<FeedViewHolder>(){
    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(mContext)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = inflater.inflate(R.layout.row, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return  rssObject.items.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.textTitle.text = rssObject.items[position].title

        Picasso.with(mContext)
                .load(rssObject.items[position].enclosure.link)
                .into(holder.imageView)


        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int, isLongClick: Boolean) {
                if(!isLongClick)
                {
                    webViewIntent(position)
                }
            }
        })
    }


    fun webViewIntent(position: Int)
    {
        val intent = Intent(mContext, WebViewActivity::class.java)
        val link = rssObject.items[position].link
        intent.putExtra("URL_FOR_WEB_VIEW", link)
        mContext.startActivity(intent)
    }
}
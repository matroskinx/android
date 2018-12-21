package com.example.vladislav.android5.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import com.example.vladislav.android5.FeedAdapter
import com.example.vladislav.android5.Managers.DownloadManager
import com.example.vladislav.android5.Models.FeedAdapter
import com.example.vladislav.android5.Models.RSSObject
import com.example.vladislav.android5.OnlineUtil.Companion.isOnline
import com.example.vladislav.android5.R
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import java.io.File
import java.io.IOException
import java.io.StringReader

class RssFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        loadRssFile()
    }
    //private val RSS_link = "http://lenta.ru/rss"
    private var RSS_link = "https://news.tut.by/rss/auto.rss"
    private val RSS_to_JSON_API = "https://api.rss2json.com/v1/api.json?rss_url="


    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    val filename = "cache.json"
    private var newsRecyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)
        val v = inflater.inflate(R.layout.rss_fragment, container, false)
        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.post {
            loadRssFile()
        }
        newsRecyclerView = v.findViewById(R.id.newsRecyclerView)
        val linearLayoutManager = LinearLayoutManager(activity?.baseContext, RecyclerView.VERTICAL, false)
        newsRecyclerView?.layoutManager = linearLayoutManager
        return v
    }

    fun loadRssFile(){
        val loadRSSAsync = @SuppressLint("StaticFieldLeak")
        object: AsyncTask<String, String, String>(){

            override fun onPreExecute() {

                swipeRefreshLayout.isRefreshing = true
            }

            override fun onPostExecute(result: String?) {

                val file = File(context?.filesDir, filename)

                val fileContents = result

                if(isOnline(context as Context) && fileContents != null)
                {
                    file.writeText(fileContents)
                    Log.d("FILERSS", "FILE SAVED")
                }


                val RssReader = JsonReader(StringReader(result))
                RssReader.isLenient = true


                val gs = GsonBuilder().create()
                val recievedRSS = gs.fromJson<RSSObject>(RssReader, RSSObject::class.java)

                val adapter = FeedAdapter(recievedRSS, activity?.baseContext as Context)
                newsRecyclerView?.adapter = adapter
                adapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }

            override fun doInBackground(vararg params: String?): String
            {
                val file = File(context?.filesDir, filename)

                if(!isOnline(context as Context) && file.exists())
                {
                    val result : String = file.readText()
                    return result
                }

                return DownloadManager.fetchJson(params[0]) as String
            }
        }

        val url_get_data = StringBuilder(RSS_to_JSON_API)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val rec = sharedPref?.getString("RSS_URL_STRING", RSS_link)

        if(rec.isNullOrEmpty()){
            Toast.makeText(context as Context, "Please set rss link in settings", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.link_dest)
            return
        }

        RSS_link = rec as String
        url_get_data.append(RSS_link)
        loadRSSAsync.execute(url_get_data.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
    }

}
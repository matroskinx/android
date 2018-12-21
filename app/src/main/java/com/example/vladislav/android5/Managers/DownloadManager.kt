package com.example.vladislav.android5.Managers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class DownloadManager {
    companion object {
        fun fetchJson(url : String?) : String? {
            val request = Request.Builder().url(url as String).build()
            var body : String? = ""
            val client = OkHttpClient()
            body = client.newCall(request).execute().body()?.string()

            return body

        }

    }
}

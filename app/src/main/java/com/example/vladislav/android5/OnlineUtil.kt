package com.example.vladislav.android5

import android.content.Context
import android.net.ConnectivityManager

class OnlineUtil
{
    companion object {
        fun isOnline(context : Context): Boolean {
            val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }
}
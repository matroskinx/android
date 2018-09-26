package com.example.vladislav.android5

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.telephony.NetworkScanRequest
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Button

import android.widget.TextView
import android.widget.Toast
import android.support.design.widget.Snackbar
import android.widget.FrameLayout
import java.util.jar.Manifest

val PHONE_STATE_PERM_CODE = 289

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.textView4).apply{
            text = BuildConfig.VERSION_NAME
        }
        GetID()
        if (BuildConfig.FLAVOR.equals("master"))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else if (BuildConfig.FLAVOR.equals("developer"))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

    }

    fun button_response(view: View)
    {
        if(checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_LONG).show()
        }
        else
        {
            requestPhonePermission()
        }
    }
    fun GetID()
    {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPhonePermission()
        }
        else
        {
            showImei()
        }
    }

    fun requestPhonePermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE))
        {
            Toast.makeText(this, "In order to see IMEI number, you have to grant phone permission", Toast.LENGTH_LONG).show()
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), PHONE_STATE_PERM_CODE)
        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), PHONE_STATE_PERM_CODE)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode)
        {
            PHONE_STATE_PERM_CODE ->
            {
                if(grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    showImei()
                }
                else
                {
                    findViewById<TextView>(R.id.textView).apply {
                        text = "Permission not granted"
                    }
                }
            }
        }

    }

    fun showImei() {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        findViewById<TextView>(R.id.textView).apply {
            text = telephonyManager.imei
        }
    }



}

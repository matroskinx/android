package com.example.vladislav.android5

import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

val PHONE_STATE_PERM_CODE = 289

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        versionTextView4.text = BuildConfig.VERSION_NAME
        GetID()
    }

    fun button_response(view: View)
    {
        if(checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
        {
            //Toast.makeText(this, "Permission already granted", Toast.LENGTH_LONG).show()
            Snackbar.make(mainLayout, getString(R.string.perm_granted), Snackbar.LENGTH_LONG).show()

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
            Snackbar.make(mainLayout, getString(R.string.perm_request), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.text_grant_button)) {
                        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), PHONE_STATE_PERM_CODE)
                    }
                    .show()

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
                    imeiTextView.text = getString(R.string.perm_not_granted)
                    Snackbar.make(mainLayout, getString(R.string.perm_warning), Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun showImei() {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        imeiTextView.text = telephonyManager.imei
    }



}

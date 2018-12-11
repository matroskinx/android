package com.example.vladislav.android5

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.*
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getSystemService
import android.Manifest


import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.about_fragment.*
import kotlinx.android.synthetic.main.about_fragment.view.*

//import java.util.jar.Manifest

val MULTIPLE_PERM_CODE = 400

class AboutFragment : Fragment() {

    private val app_permissions = arrayOf<String>(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)

    private lateinit var aboutView : View

    override fun onCreateView(inflater: LayoutInflater, container : ViewGroup?,
                              savedInstanceState: Bundle?) : View?
    {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        aboutView = view

        request_button.setOnClickListener{
            version_view.text = BuildConfig.VERSION_NAME
            fragmentReqBtn(view)
        }

        if(checkSelfPermission(context!!, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPhonePermission()
        }

        imei_textview.text = getImei()

    }


    fun fragmentReqBtn(view : View)
    {
            if (checkSelfPermission(context!!, Manifest.permission.READ_PHONE_STATE ) != PERMISSION_GRANTED ||
                    checkSelfPermission(context!!, Manifest.permission.CAMERA) != PERMISSION_GRANTED ||
                    checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED)
            {
                requestPhonePermission()
                Snackbar.make(aboutView, "ONE OR TWO PERMISSION ARE NOT GRANTED", Snackbar.LENGTH_LONG).show()
            }
            else
            {
                imei_textview.text = getImei()
                Snackbar.make(aboutView, "ALL PERMISSION ARE GRANTED", Snackbar.LENGTH_LONG).show()
            }
    }

    fun requestPhonePermission()
    {
        if(shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE) ||
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            Snackbar.make(aboutView, "GRANT EVERYTHING PLS", Snackbar.LENGTH_LONG).show()
        }
        requestPermissions(app_permissions, MULTIPLE_PERM_CODE)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode)
        {
            PHONE_STATE_PERM_CODE ->
            {
                if(grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    imei_textview.text = getImei()
                }
                else
                {
                    imei_textview.text = getString(R.string.perm_not_granted)
                    Snackbar.make(aboutView, getString(R.string.perm_warning), Snackbar.LENGTH_SHORT).show()
                }
            }
            MULTIPLE_PERM_CODE ->
            {
                Snackbar.make(aboutView, "Multiple permissions", Snackbar.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun getImei() : String
    {
        //val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        ///imeiTextView.text = telephonyManager.imei
        val tm = activity?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm?.imei
    }



// для добавления элементов в appbar которые будут показываться только для данного фрагмента
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.main_menu, menu)
//    }

}
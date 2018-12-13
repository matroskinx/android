package com.example.vladislav.android5

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.ActivityCompat
import android.telephony.TelephonyManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference


val PHONE_STATE_PERM_CODE = 289

var testString : String? = null

class MainActivity : AppCompatActivity() {

    var mHost : NavHostFragment? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        testString = savedInstanceState?.getString("testKey")
        //versionTextView4.text = BuildConfig.VERSION_NAME

//        if(savedInstanceState != null)
//        {
//            //imeiTextView.text = savedInstanceState.getString("savedImei")
//        }
//        else
//        {
//            if(checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
//            {
//                showImei()
//            }
//            else
//            {
//                requestPhonePermission()
//            }
//        }

//        var host: NavHostFragment = supportFragmentManager.
//                findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        mHost = supportFragmentManager.
                findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

//        var x = mHost?.fragmentManager!!
//
//
//        var z = x.fragments[0].id

        //supportFragmentManager.saveFragmentInstanceState(mHost)

//        if(savedInstanceState != null) {
//            mHost = mHost?.fragmentManager?.getFragment(savedInstanceState, "myfr") as NavHostFragment?
//        }

//        if(savedInstanceState != null) {
//
//            host = supportFragmentManager.getFragment(savedInstanceState, "myfr") as NavHostFragment? ?: return
//        }
//        host.fragmentManager.
//        mContent = host


        val navController = mHost?.navController!!

        setupBottomNavMenu(navController)

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)

//        mContent = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
//
//        var b = supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
//        var c = supportFragmentManager.findFragmentById(R.id.home_dest)
//        //var d = supportFragmentManager.findFragmentById(R.id.)
//        var a = supportFragmentManager.fragments[0].id
    }

    override fun onBackPressed() {
        val f = mHost?.fragmentManager?.findFragmentById(R.id.my_nav_host_fragment)

        super.onBackPressed()
    }

//    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
//        super.onSaveInstanceState(outState, outPersistentState)
//
//        if(outState != null)
//        {
//            mHost?.fragmentManager?.putFragment(outState,"myft", mHost?.fragmentManager?.findFragmentById(R.id.home_dest)!!)
//        }
//
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean
    {
        return navigateUp(findNavController(R.id.my_nav_host_fragment), null)
    }

    private fun setupBottomNavMenu(navController: NavController) {

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun button_response(view: View)
    {
        if(checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
        {
            Snackbar.make(mainLayout, getString(R.string.perm_granted), Snackbar.LENGTH_LONG).show()

        }
        else
        {
            //requestPhonePermission()
        }
    }



    fun showImei() {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        ///imeiTextView.text = telephonyManager.imei
    }
}

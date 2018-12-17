package com.example.vladislav.android5

import android.app.AlertDialog
import android.app.ProgressDialog
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
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.*
import com.example.vladislav.android5.R.styleable.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference


val PHONE_STATE_PERM_CODE = 289

var testString : String? = null

class MainActivity : AppCompatActivity() {

    var mHost : NavHostFragment? = null
    lateinit var bottom_bar : BottomNavigationView
    lateinit var navController : NavController


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHost = supportFragmentManager.
                findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return


        navController = mHost?.navController!!

        bottom_bar = bottom_nav_view

        setupBottomNavMenu(navController)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onSupportNavigateUp(): Boolean
    {
        return navigateUp(findNavController(R.id.my_nav_host_fragment), null)
    }

    private fun setupBottomNavMenu(navController: NavController) {

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)

        bottomNav?.setupWithNavController(navController)



        bottom_nav_view.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->

                val navHostfragment = this.supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)

                val fr = navHostfragment?.childFragmentManager?.fragments?.get(0)

                if(fr is UserFragment)
                {
                    val newfr = fr as UserFragment
                }
                else
                {
                    Toast.makeText(this, "hello" + item.itemId, Toast.LENGTH_SHORT).show()
                }

                when (item.itemId) {
                    R.id.home_dest -> {
                        // TODO
                        navController.navigate(R.id.home_dest)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.status_dest -> {
                        // TODO
                        navController.navigate(R.id.status_dest)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.user_dest -> {
                        // TODO
                        navController.navigate(R.id.user_dest)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        if (i == R.id.logoff_button) {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return true
        }
        else if(i == R.id.about_dest)
        {
            return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
        }
        else
        {
            return super.onOptionsItemSelected(item)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }



}

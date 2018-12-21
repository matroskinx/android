package com.example.vladislav.android5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.*
import com.example.vladislav.android5.Interfaces.ISwitchActionBar
import com.example.vladislav.android5.Interfaces.ISwitchBottomNavigation
import com.google.firebase.auth.FirebaseAuth


val PHONE_STATE_PERM_CODE = 289

var actionMenu : Menu? = null

class MainActivity : AppCompatActivity(), ISwitchBottomNavigation, ISwitchActionBar {

    var mHost : NavHostFragment? = null
    lateinit var navController : NavController


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHost = supportFragmentManager.
                findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return

        navController = mHost?.navController as NavController

        setupBottomNavMenu(navController)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        actionMenu = menu
        return true
    }


    override fun onSupportNavigateUp(): Boolean
    {
        return navigateUp(findNavController(R.id.my_nav_host_fragment), null)
    }

    private fun setupBottomNavMenu(navController: NavController) {

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)

        bottomNav?.setupWithNavController(navController)

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

    override fun hideBottomNav()
    {
        bottom_nav_view.visibility = View.GONE
    }

    override fun showBottomNav()
    {
        bottom_nav_view.visibility = View.VISIBLE
    }

    override fun hideActionBarArrow() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeButtonEnabled(false)
        }

        var size = 0
        if(actionMenu != null)
        {
            size = actionMenu?.size()!!
        }

        for (i in 0..size-1)
        {
            actionMenu?.getItem(i)?.setVisible(false)
        }

    }

    override fun showActionBarArrow() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        var size = 0
        if(actionMenu != null)
        {
            size = actionMenu?.size()!!
        }

        for (i in 0..size-1)
        {
            actionMenu?.getItem(i)?.setVisible(true)
        }
    }


}

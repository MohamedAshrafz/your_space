package com.example.your_space.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.WindowCompat
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.your_space.R
import com.example.your_space.databinding.ActivityMainBinding
import com.example.your_space.ui.authentication.AuthenticationActivity
import com.firebase.ui.auth.AuthUI

class MainActivity : AppCompatActivity() {

    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // setting the drawer navigation with the navController
        binding.navView.setupWithNavController(navController)
        // setting the bottom navigation with the navController
        binding.bottomNavigation.setupWithNavController(navController)

        val listOfMenuItems = mutableListOf<MenuItem>()
        binding.bottomNavigation.menu.forEach { item ->
            listOfMenuItems.add(item)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->

                if (listOfMenuItems.any { it.itemId == destination.id }) {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
                else{
                    binding.bottomNavigation.visibility = View.GONE
                }
        }


//        // getting the app bar configuration
//        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(navController, binding.drawerLayout)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.logout -> {
                // logout implementation
                // signing-out the current user
                // we have to wait till sing-out complete
                // otherwise AuthenticationActivity will launch this activity again
                AuthUI.getInstance().signOut(this.applicationContext).addOnSuccessListener {
                    // finishing the current activity
                    finish()

                    // launch the sign-in activity
                    val authenticationActivityIntent =
                        Intent(
                            this.applicationContext,
                            AuthenticationActivity::class.java
                        )
                    startActivity(authenticationActivityIntent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(binding.drawerLayout)
                || super.onSupportNavigateUp()
    }
}
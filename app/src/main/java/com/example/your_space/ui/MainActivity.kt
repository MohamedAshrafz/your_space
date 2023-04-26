package com.example.your_space.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.your_space.R
import com.example.your_space.databinding.ActivityMainBinding
import com.example.your_space.ui.authentication.AuthenticationActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    lateinit var toggle: ActionBarDrawerToggle

    // for saving the state of error_snackbar to dismiss it later when the location is enabled
    var locationErrorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // getting the main layout components
        navController = findNavController(R.id.nav_host_fragment_content_main)
        val bindingDrawerLayout = binding.drawerLayout
        val bindingNavigationView = binding.navView
        val bindingToolbar = binding.toolbar
        val bindingBottomNavigation = binding.bottomNavigation

        val listOfMenuItems = mutableListOf<MenuItem>()
        binding.bottomNavigation.menu.forEach { item ->
            listOfMenuItems.add(item)
        }

        setSupportActionBar(bindingToolbar)

        // setting the drawer navigation with the navController
        bindingNavigationView.setupWithNavController(navController)

        // setting the bottom navigation with the navController
        bindingBottomNavigation.setupWithNavController(navController)

        // getting the app bar configuration
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.Home,
                R.id.SecondFragment,
                R.id.bookingFragment,
                R.id.mapsFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        toggle = ActionBarDrawerToggle(
            this,
            bindingDrawerLayout,
            bindingToolbar,
            R.string.drawer_opened_content_desc,
            R.string.drawer_closed_content_desc
        )

        bindingDrawerLayout.addDrawerListener(toggle)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (listOfMenuItems.any { it.itemId == destination.id }) {
                binding.bottomNavigation.visibility = View.VISIBLE
                // Set the drawer icon to appear on the "home" button
                toggle.isDrawerIndicatorEnabled = true
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                toggle.syncState()
            } else {
                binding.bottomNavigation.visibility = View.GONE
                // Set navigate up button to visible
                toggle.isDrawerIndicatorEnabled = false
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                toggle.syncState()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val currentDestination = navController.currentDestination

        val listOfMenuItems = mutableListOf<MenuItem>()
        binding.bottomNavigation.menu.forEach { item ->
            listOfMenuItems.add(item)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->

            val item: MenuItem = binding.toolbar.menu.findItem(R.id.logout)

            item.isVisible = listOfMenuItems.any { it.itemId == destination.id }
        }

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
package com.example.your_space.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.your_space.R
import com.example.your_space.databinding.ActivityMainBinding
import com.example.your_space.repository.AppRepository
import com.example.your_space.ui.authentication.AuthenticationActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

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


//        // getting the current user data
//        val myIntent = intent
//
//        val user = intent.getParcelableExtra<UserDB>(AuthenticationActivity.USER_DATA)
//
//        if (user != null) {
//            Log.e("Main Activity", user.toString())
//        } else {
//            Log.e("Main Activity", "cannot get the user")
//        }

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
                R.id.messagesFragment
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

    override fun onStart() {
        super.onStart()
        val sp = getSharedPreferences(AuthenticationActivity.LOGIN_STATE, MODE_PRIVATE)

        val userId = sp.getString(AuthenticationActivity.USER_ID, null)

        if (userId != null) {
            reGetTokenAndUser(userId)
            Log.i("SP UserId", userId.toString())
        } else {
            Log.e("Main Activity", "cannot get the user")
        }
    }

    private fun reGetTokenAndUser(userId: String) {
        val repository = AppRepository.getInstance(applicationContext)
        lifecycleScope.launch {
            val user = repository.updateTokenForUserWithUserId(userId)
//            val view = window.decorView

            try {
                if (user == null) {
                    Snackbar.make(
                        binding.root.findViewById(R.id.nav_host_fragment_content_main),
                        getString(R.string.connection_error),
                        Snackbar.LENGTH_LONG
                    ).show()
                } else if (user.userId == "-1") {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.there_was_a_problem_in_logging_you_in),
                        Toast.LENGTH_LONG
                    ).show()
                    logoutFlow()
                } else {
                    Snackbar.make(
                        binding.root.findViewById(R.id.nav_host_fragment_content_main),
                        getString(R.string.you_are_connected),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Log.e(
                    "Error showing Snackbar",
                    e.printStackTrace().toString()
                )
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
                logoutFlow()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logoutFlow() {
        // logout implementation
        // signing-out the current user
        // we have to wait till sing-out complete
        // otherwise AuthenticationActivity will launch this activity again
        // launch the sign-in activity
        val sp = getSharedPreferences(AuthenticationActivity.LOGIN_STATE, MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(AuthenticationActivity.USER_ID, null)
        editor.apply()

        // launch the sign-in activity
        val authenticationActivityIntent =
            Intent(
                this.applicationContext,
                AuthenticationActivity::class.java
            )
        startActivity(authenticationActivityIntent)

        this.finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(binding.drawerLayout)
                || super.onSupportNavigateUp()
    }
}
package com.example.healthify.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.healthify.R
import com.example.healthify.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val bottomNavView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavView.visibility = if (
                destination.id == R.id.settingsFragment ||
                destination.id == R.id.runFragment ||
                destination.id == R.id.statisticsFragment
            ) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        NavigationUI.setupWithNavController(bottomNavView, navController)

        navigateToTrackingFragmentIfNeeded(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_TRACKING_FRAGMENT && this::navController.isInitialized) {
            navController.navigate(R.id.action_global_tracking)
        }
    }
}

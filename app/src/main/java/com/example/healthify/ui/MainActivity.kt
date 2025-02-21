package com.example.healthify.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.healthify.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bottomNavView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

       navHostFragment.findNavController()
           .addOnDestinationChangedListener{
               _ , destination , _ ->
               when(destination.id){
                   R.id.settingsFragment , R.id.runFragment , R.id.statisticsFragment ->
                       bottomNavView.visibility = View.VISIBLE
                   else -> bottomNavView.visibility = View.GONE
               }
           }
        val navController: NavController = navHostFragment.navController

        NavigationUI.setupWithNavController(bottomNavView, navController)
    }
}
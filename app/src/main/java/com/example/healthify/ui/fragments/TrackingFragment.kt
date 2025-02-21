package com.example.healthify.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.healthify.R
import com.example.healthify.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.healthify.services.TrackingService
import com.example.healthify.ui.viewModels.MainViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private val viewModel : MainViewModel by viewModels()
    private var map : GoogleMap? = null
    private var mapView : MapView?  = null
    private lateinit var btnToggle : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnToggle = view.findViewById(R.id.btnToggleRun)
        btnToggle.setOnClickListener {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }

        mapView = view.findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)


        mapView?.getMapAsync {
            map = it
        }
    }

    private fun sendCommandToService(action:String) =
        Intent(requireContext() , TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    @Deprecated("Deprecated in Java")
    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}
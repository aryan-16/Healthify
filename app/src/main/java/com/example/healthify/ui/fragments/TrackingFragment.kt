package com.example.healthify.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.healthify.R
import com.example.healthify.other.Constants.ACTION_PAUSE_SERVICE
import com.example.healthify.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.healthify.other.Constants.MAPS_ZOOM
import com.example.healthify.other.Constants.POLYLINE_COLOR
import com.example.healthify.other.Constants.POLYLINE_WIDTH
import com.example.healthify.other.TrackingUtility
import com.example.healthify.services.Polyline
import com.example.healthify.services.TrackingService
import com.example.healthify.ui.viewModels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private val viewModel: MainViewModel by viewModels()
    private var map: GoogleMap? = null
    private var mapView: MapView? = null
    private lateinit var btnToggle: Button
    private lateinit var btnFinish: Button
    private lateinit var tvTimer: TextView
    private var currentTimeMillis  = 0L

    private var isTracking: Boolean = false
    private var pathPoints = mutableListOf<Polyline>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnToggle = view.findViewById(R.id.btnToggleRun)
        btnFinish = view.findViewById(R.id.btnFinishRun)
        tvTimer = view.findViewById(R.id.tvTimer)

        //
        btnToggle.setOnClickListener {
            checkAndRequestNotificationPermission()
            toggleRun()
        }

        mapView = view.findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)

        mapView?.getMapAsync {
            map = it
            addAllPolylines()
            addStartEndMarkers() // ✅ Add start & end markers
        }
        subscribeToObservers()
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            } else {
                sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            }
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            } else {
                Toast.makeText(requireContext(), "Notification permission required!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })
        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
            addStartEndMarkers() // ✅ Update markers on path change
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner , Observer {
            currentTimeMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(currentTimeMillis , true)
            tvTimer.text = formattedTime
        })
    }

    private fun toggleRun() {
        if (isTracking) {
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking) {
            btnToggle.text = "Start"
            btnFinish.visibility = View.VISIBLE
        } else {
            btnToggle.text = "Stop"
            btnFinish.visibility = View.GONE
        }
    }

    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAPS_ZOOM
                )
            )
        }
    }

    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addStartEndMarkers() {
        map?.clear() // Clear previous markers & polylines
        addAllPolylines()

        if (pathPoints.isNotEmpty() && pathPoints.first().isNotEmpty()) {
            val startPoint = pathPoints.first().first()
            map?.addMarker(
                MarkerOptions()
                    .position(startPoint)
                    .title("Start")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
        }

        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            val endPoint = pathPoints.last().last()
            map?.addMarker(
                MarkerOptions()
                    .position(endPoint)
                    .title("End")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }
}

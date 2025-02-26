package com.example.healthify.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.healthify.R
import com.example.healthify.other.Constants.ACTION_PAUSE_SERVICE
import com.example.healthify.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.healthify.other.Constants.ACTION_STOP_SERVICE
import com.example.healthify.other.Constants.FASTEST_LOCATION_INTERVAL
import com.example.healthify.other.Constants.LOCATION_UPDATE_INTERVAL
import com.example.healthify.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.healthify.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.healthify.other.Constants.NOTIFICATION_ID
import com.example.healthify.other.Constants.TIMER_UPDATE_INTERVAL
import com.example.healthify.other.TrackingUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>


@AndroidEntryPoint
class TrackingService : LifecycleService() {

    private var isFirstRun = true
    var serviceKilled = false

    @Inject
     lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var BaseNotificationBuilder : NotificationCompat.Builder

    lateinit var currNotificationBuilder : NotificationCompat.Builder

    val timeRunInSeconds = MutableLiveData<Long>()


    companion object {
        val timeRunInMillis = MutableLiveData<Long>()

        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        timeRunInMillis.postValue(0L)
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()

        currNotificationBuilder = BaseNotificationBuilder

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (!serviceKilled){
            isTracking.observe(this, Observer { isTracking ->
                updateLocationTracking(isTracking)
                updateNotificationTrackingState(isTracking)
            })
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Timber.d("Resuming service")
                        isTracking.postValue(true)
                        startTimer()
                    }
                }

                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Pausing service")
                    pauseService()
                }

                ACTION_STOP_SERVICE -> {
                    killService()
                }
            }
        }
        return START_STICKY
    }

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun= 0L
    private var timeStarted = 0L
    private var lastSecondTimeStamp = 0L

    private fun startTimer() {
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis() // Set start time
        isTimerEnabled = true

        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value == true) {
                lapTime = System.currentTimeMillis() - timeStarted
                timeRunInMillis.postValue(timeRun + lapTime) // Use stored timeRun

                if (timeRunInMillis.value!! >= lastSecondTimeStamp + 1000L) {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimeStamp += 1000L
                }
                delay(TIMER_UPDATE_INTERVAL)
            }
            timeRun += lapTime  // Store elapsed time after stopping
        }
    }


    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
        timeRun += lapTime  // Store elapsed time before stopping
    }


    private fun stopService() {
        isTracking.postValue(false)
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        stopForeground(true)
        stopSelf()
    }

    private fun killService(){
        serviceKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    private fun updateNotificationTrackingState(isTracking: Boolean){
        val notificationActionText = if(isTracking) "Pause" else "Resume"

        val pendingIntent = if(isTracking){
            val pauseIntent  = Intent (this , TrackingService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this , 1 , pauseIntent , FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            }else{
                val resumeIntent = Intent(this , TrackingService::class.java).apply {
                    action = ACTION_START_OR_RESUME_SERVICE
                }
            PendingIntent.getService(this , 2, resumeIntent , FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        currNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            // remove all actions , adding empty array list
            set(currNotificationBuilder, ArrayList<NotificationCompat.Action>())

        }
        if (!serviceKilled){
            currNotificationBuilder = BaseNotificationBuilder
                .addAction(R.drawable.ic_pause_black_24dp, notificationActionText , pendingIntent)
            notificationManager.notify(NOTIFICATION_ID , currNotificationBuilder.build())
        }

    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtility.hasLocationPermissions(this)) {
                val request = LocationRequest.Builder(PRIORITY_HIGH_ACCURACY, LOCATION_UPDATE_INTERVAL)
                    .setMinUpdateIntervalMillis(FASTEST_LOCATION_INTERVAL)
                    .build()

                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value == true) {
                result.locations?.forEach { location ->
                    addPathPoint(location)
                    Timber.d("New Location: ${location.latitude}, ${location.longitude}")
                }
            }
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(it.latitude, it.longitude)
            pathPoints.value?.apply {
                lastOrNull()?.add(pos) ?: add(mutableListOf(pos))
                pathPoints.postValue(this)
            }
        }
    }

    private fun addEmptyPolyline() {
        pathPoints.value?.apply {
            add(mutableListOf())
            pathPoints.postValue(this)
        } ?: pathPoints.postValue(mutableListOf(mutableListOf()))
    }

    private fun startForegroundService() {
            startTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }


// using hilt
        startForeground(NOTIFICATION_ID, BaseNotificationBuilder.build())

        timeRunInSeconds.observe(this , Observer {
            val notification = currNotificationBuilder
                .setContentText(TrackingUtility.getFormattedStopWatchTime(it * 1000L))
            notificationManager.notify(NOTIFICATION_ID , notification.build())
        })
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}

package com.example.healthify.services

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import com.example.healthify.db.StepEntry
import com.example.healthify.repositories.StepRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StepDetectorService : Service(), SensorEventListener {

    @Inject
    lateinit var repository: StepRepository  // ✅ Inject Repository instead of ViewModel

    private lateinit var sensorManager: SensorManager

    override fun onCreate() {
        super.onCreate()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            Log.e("StepDetectorService", "No Step Counter sensor found!")
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val stepCount = event.values[0].toInt()
            Log.d("StepDetectorService", "New step detected: $stepCount")

            val stepEntry = StepEntry(
                timestamp = System.currentTimeMillis(),
                stepCount = stepCount
            )

            // ✅ Save step entry using repository inside a coroutine
            CoroutineScope(Dispatchers.IO).launch {
                repository.insertStep(stepEntry)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

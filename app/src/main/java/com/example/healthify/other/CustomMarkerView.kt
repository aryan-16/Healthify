package com.example.healthify.other

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.example.healthify.R
import com.example.healthify.db.Run
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("ViewConstructor")
class CustomMarkerView (
    private val runs : List<Run>,
    c : Context,
    layoutId : Int
) : MarkerView(c , layoutId){

    override fun getOffset(): MPPointF {
        return MPPointF(-width/2f , -height.toFloat())

    }
    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if(e == null){
            return
        }
        val currRunID = e.x.toInt()
        val run = runs[currRunID]

        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timestamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        findViewById<TextView>(R.id.tvDateMV).text = dateFormat.format(calendar.time)

        // Set other text views
        findViewById<TextView>(R.id.tvAvgSpeedMV).text = "${run.avgSpeedInKMH}km/hr"
        findViewById<TextView>(R.id.tvDistanceMV).text = "${run.distanceInMeters / 1000f} km"
        findViewById<TextView>(R.id.tvCaloriesBurnedMV).text = "${run.caloriesBurned} kcal"
        findViewById<TextView>(R.id.tvDurationMV).text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)
    }
}
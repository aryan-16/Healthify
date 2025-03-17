package com.example.healthify.ui.fragments

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.healthify.R
import com.example.healthify.other.CustomMarkerView
import com.example.healthify.other.TrackingUtility
import com.example.healthify.ui.viewModels.StatisticsViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round


@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private val viewModel : StatisticsViewModel by viewModels()
    private lateinit var tvTotalTime : TextView
    private lateinit var tvTotalDistance : TextView
    private lateinit var tvTotalAvgSpeed : TextView
    private lateinit var tvTotalCaloriesBurned : TextView
    private lateinit var barChart : BarChart

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTotalTime = view.findViewById(R.id.tvTotalTime)
        tvTotalDistance = view.findViewById(R.id.tvTotalDistance)
        tvTotalAvgSpeed = view.findViewById(R.id.tvAverageSpeed)
        tvTotalCaloriesBurned = view.findViewById(R.id.tvTotalCalories)
        barChart = view.findViewById(R.id.BarChart)

        subscribeToObservers()
        setupBarChart()

    }

    private fun setupBarChart(){
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)

        }
        barChart.axisLeft.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }
        barChart.axisRight.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }
        barChart.apply {
            description.text = "Avg Speed Over Time"
            description.textSize = 20f
            legend.isEnabled = false
            description.textAlign = Paint.Align.RIGHT // Ensure text is centered
        }
    }


    private fun subscribeToObservers(){

        viewModel.totalTimeRun.observe(viewLifecycleOwner , Observer {
            it?.let {
                val TotalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
                tvTotalTime.text = TotalTimeRun
            }
        })

        viewModel.totalDistance.observe(viewLifecycleOwner , Observer {
           it?.let {
               val km = it / 1000f
               val totalDistance = round(km*10f) / 10f
               val totalDistanceString = "${totalDistance}km"
               tvTotalDistance.text = totalDistanceString

           }
        })

        viewModel.totalAvgSpeed.observe(viewLifecycleOwner , Observer {
            it?.let {
                val avgSpeed = round(it*10f) / 10f
                val avgSpeedString = "${avgSpeed}km/h"
                tvTotalAvgSpeed.text = avgSpeedString
            }
        })

        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner , Observer {
            it?.let {
                val caloriesBurned = "${it}kcal"
                tvTotalCaloriesBurned.text = caloriesBurned
            }
        })
        viewModel.runsSortedByDate.observe(viewLifecycleOwner , Observer {
            it?.let {
                val allAvgSpeeds = it.indices.map{i-> BarEntry(i.toFloat() , it[i].avgSpeedInKMH)}
                val bardataset = BarDataSet(allAvgSpeeds , "Avg Speed Over Time").apply {
                    valueTextColor = Color.BLUE
                    color =   ContextCompat.getColor(requireContext() , R.color.md_blue_50)
                }
                barChart.data = BarData(bardataset)
                barChart.marker = CustomMarkerView(it.reversed() , requireContext() , R.layout.marker_view)
                barChart.invalidate()
            }
        })
    }
}
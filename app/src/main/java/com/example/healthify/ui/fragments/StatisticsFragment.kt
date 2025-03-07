package com.example.healthify.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.healthify.R
import com.example.healthify.other.TrackingUtility
import com.example.healthify.ui.viewModels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round


@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {
    private val viewModel : StatisticsViewModel by viewModels()
    private lateinit var tvTotalTime : TextView
    private lateinit var tvTotalDistance : TextView
    private lateinit var tvTotalAvgSpeed : TextView
    private lateinit var tvTotalCaloriesBurned : TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTotalTime = view.findViewById(R.id.tvTotalTime)
        tvTotalDistance = view.findViewById(R.id.tvTotalDistance)
        tvTotalAvgSpeed = view.findViewById(R.id.tvAverageSpeed)
        tvTotalCaloriesBurned = view.findViewById(R.id.tvTotalCalories)

        subscribeToObservers()

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
    }
}
package com.example.healthify.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.healthify.R
import com.example.healthify.db.StepEntry
import com.example.healthify.viewModels.StepViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StepStatisticsFragment : Fragment() {

    private val viewModel: StepViewModel by viewModels()
    private lateinit var barChart: BarChart
    private lateinit var tvTotalSteps: TextView
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 5000L // Update every 5 seconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_step_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTotalSteps = view.findViewById(R.id.tvTotalSteps)
        barChart = view.findViewById(R.id.barChart)

        viewModel.allSteps.observe(viewLifecycleOwner) { stepEntries ->
            updateBarChart(stepEntries)
        }

        viewModel.totalSteps.observe(viewLifecycleOwner) { totalSteps ->
            tvTotalSteps.text = "Total Steps: $totalSteps"
        }

        startAutoRefresh()
    }

    private fun updateBarChart(stepEntries: List<StepEntry>) {
        val entries = stepEntries.mapIndexed { index, stepEntry ->
            BarEntry(index.toFloat(), stepEntry.stepCount.toFloat())
        }

        val barDataSet = BarDataSet(entries, "Steps Count").apply {
            color = resources.getColor(R.color.md_blue_900)
        }
        val barData = BarData(barDataSet)

        barChart.apply {
            data = barData
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            description.isEnabled = false
            animateY(1000)
            invalidate()  // Refresh the chart
        }
    }

    private fun startAutoRefresh() {
        val refreshRunnable = object : Runnable {
            override fun run() {
                viewModel.fetchLatestSteps()  // Manually trigger a refresh
                handler.postDelayed(this, updateInterval)
            }
        }
        handler.post(refreshRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null) // Stop updates when fragment is destroyed
    }
}

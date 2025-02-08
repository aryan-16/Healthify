package com.example.healthify.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthify.R

class SetupFragment : Fragment(R.layout.fragment_setup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvContinue: TextView = view.findViewById(R.id.tvContinue)

        // Set up click listener
        tvContinue.setOnClickListener {
            // Navigate to another fragment using Navigation Component
            findNavController().navigate(R.id.action_setupFragment_to_runFragment)
        }
    }
}
package com.example.healthify.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.healthify.R
import com.example.healthify.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.healthify.other.Constants.KEY_NAME
import com.example.healthify.other.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SetupFragment : Fragment(R.layout.fragment_setup) {


    private lateinit var etName : EditText
    private lateinit var etWeight : EditText
    private lateinit var tvToolbarTitle : TextView


@Inject
lateinit var sharedPref : SharedPreferences

@set:Inject
var isFirstAppOpen  = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        etName = view.findViewById(R.id.etName)
        etWeight = view.findViewById(R.id.etWeight)
        tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle)


        if(!isFirstAppOpen){
            // remove setup fragment from back stack if user is not opening the app for first time
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment , true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }
        val tvContinue: TextView = view.findViewById(R.id.tvContinue)

        // Set up click listener
        tvContinue.setOnClickListener {
            val success = writeDataToSharedPref()
            if (success){
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            }
            else{
             Snackbar.make(requireView() , "Please enter all the fields", Snackbar.LENGTH_SHORT).show()

            }

        }
    }

    private fun writeDataToSharedPref():Boolean{
        val name = etName.text.toString()
        val weight = etWeight.text.toString()

        if(name.isEmpty() || weight.isEmpty()){
            return false
        }
    sharedPref.edit()
        .putString(KEY_NAME , name)
        .putFloat(KEY_WEIGHT , weight.toFloat())
        .putBoolean(KEY_FIRST_TIME_TOGGLE , false)
        .apply() // async
        val toolbarText = "Let's go $name!"
        (requireActivity().findViewById<TextView>(R.id.tvToolbarTitle)).text = toolbarText
    return true
    }
}
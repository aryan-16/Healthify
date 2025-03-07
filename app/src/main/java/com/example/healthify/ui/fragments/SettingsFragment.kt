package com.example.healthify.ui.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.healthify.R
import com.example.healthify.other.Constants.KEY_NAME
import com.example.healthify.other.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var etName : EditText
    private lateinit var etWeight : EditText
    private lateinit var btnApplyChanges : Button


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etName = view.findViewById(R.id.etName)
        etWeight = view.findViewById(R.id.etWeight)
        btnApplyChanges = view.findViewById(R.id.btnApplyChanges)


        loadFieldsFromSharedPref()

        btnApplyChanges.setOnClickListener{
            val success = applyChangesToSharedPref()
            if(success){
                Snackbar.make(view , "Saved Changes", Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(view, "Please fill out all the fields" , Snackbar.LENGTH_LONG).show()

            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun loadFieldsFromSharedPref (){
        val name = sharedPreferences.getString(KEY_NAME , "")
        val weight = sharedPreferences.getFloat(KEY_WEIGHT , 80f)
        etName.setText(name)
        etWeight.setText(weight.toString())
    }

    private fun applyChangesToSharedPref():Boolean{
    val nameText= view?.findViewById<EditText>(R.id.etName)?.text.toString()
        val weightText = view?.findViewById<EditText>(R.id.etWeight)?.text.toString()
    if(nameText.isEmpty() || weightText.isEmpty()){
        return false
    }
        sharedPreferences.edit()
            .putString(KEY_NAME , nameText)
            .putFloat(KEY_WEIGHT , weightText.toFloat())
            .apply()
        val toolbarText = "Let's go $nameText!"
        (requireActivity().findViewById<TextView>(R.id.tvToolbarTitle)).text = toolbarText
        return true

    }

}
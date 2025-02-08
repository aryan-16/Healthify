package com.example.healthify.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.healthify.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
) :ViewModel(){
}
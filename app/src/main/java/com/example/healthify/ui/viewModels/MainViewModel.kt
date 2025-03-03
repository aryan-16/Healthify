package com.example.healthify.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthify.db.Run
import com.example.healthify.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
) :ViewModel(){


    val runSortedByDate = mainRepository.getAllRunsSortedByDate()

    fun insertRun ( run : Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }

}
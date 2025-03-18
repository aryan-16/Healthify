package com.example.healthify.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthify.db.StepEntry
import com.example.healthify.repositories.StepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StepViewModel @Inject constructor(
    private val repository: StepRepository
) : ViewModel() {

    val totalSteps: LiveData<Int> = repository.getTotalSteps()
    val allSteps: LiveData<List<StepEntry>> = repository.getAllSteps()

    private val _latestSteps = MutableLiveData<List<StepEntry>>()
    val latestSteps: LiveData<List<StepEntry>> = _latestSteps

    fun insertStep(stepEntry: StepEntry) {
        viewModelScope.launch {
            repository.insertStep(stepEntry)
        }
    }

    // Function to manually refresh steps
    fun fetchLatestSteps() {
        viewModelScope.launch {
            _latestSteps.postValue(repository.getAllSteps().value)
        }
    }
}

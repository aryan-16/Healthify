package com.example.healthify.ui.viewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthify.db.Run
import com.example.healthify.other.SortType
import com.example.healthify.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
) :ViewModel(){


    val runSortedByDate = mainRepository.getAllRunsSortedByDate()
    private val runSortedByDistance = mainRepository.getAllRunsSortedByDistance()
    private val runSortedByCaloriesBurned = mainRepository.getAllRunsSortedByCaloriesBurned()
    private val runSortedByTime = mainRepository.getAllRunsSortedByTime()
    private val runSortedByAvgSpeed = mainRepository.getAllRunsSortedByAvgSpeed()

    val runs = MediatorLiveData<List<Run>>()

    var sortType = SortType.DATE

    init {
        runs.addSource(runSortedByDate){result->
            if (sortType == SortType.DATE){
                result?.let { runs.value = it }
            }
        }

        runs.addSource(runSortedByDistance){result->
            if (sortType == SortType.DISTANCE){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByCaloriesBurned){result->
            if (sortType == SortType.CALORIES_BURNED){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByAvgSpeed){result->
            if (sortType == SortType.AVG_SPEED){
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByTime){result->
            if (sortType == SortType.RUNNING_TIME){
                result?.let { runs.value = it }
            }
        }
    }



    fun sortRuns(sortType: SortType) {
        if (this.sortType == sortType) return // Avoid redundant updates

        this.sortType = sortType

        runs.apply {
            removeSource(runSortedByDate)
            removeSource(runSortedByDistance)
            removeSource(runSortedByCaloriesBurned)
            removeSource(runSortedByTime)
            removeSource(runSortedByAvgSpeed)

            when (sortType) {
                SortType.DATE -> addSource(runSortedByDate) { value = it }
                SortType.DISTANCE -> addSource(runSortedByDistance) { value = it }
                SortType.CALORIES_BURNED -> addSource(runSortedByCaloriesBurned) { value = it }
                SortType.RUNNING_TIME -> addSource(runSortedByTime) { value = it }
                SortType.AVG_SPEED -> addSource(runSortedByAvgSpeed) { value = it }
            }
        }
    }


    fun insertRun ( run : Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }

}
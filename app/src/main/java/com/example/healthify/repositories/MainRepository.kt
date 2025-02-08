package com.example.healthify.repositories

import com.example.healthify.db.Run
import com.example.healthify.db.RunDAO
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDAO: RunDAO
) {
     suspend fun insertRun(run : Run) = runDAO.insertRun(run)

    suspend fun deleteRun(run: Run) = runDAO.deleteRun(run)

    fun getAllRunsSortedByDate() = runDAO.getAllRunsSortedByDate()
    fun getAllRunsSortedByDistance() = runDAO.getAllRunsSortedByDistanceInMeters()
    fun getAllRunsSortedByTime() = runDAO.getAllRunsSortedByTimeInMillis()
    fun getAllRunsSortedByAvgSpeed() = runDAO.getAllRunsSortedByAvgSpeedInKMH()
    fun getAllRunsSortedByCaloriesBurned() = runDAO.getAllRunsSortedByCaloriesBurned()

    fun getTotalAvgSpeed() = runDAO.getTotalAvgSpeed()
    fun getTotalDistance() = runDAO.getTotalDistanceInMeters()
    fun getTotalTime()     = runDAO.getTotalTimeInMillis()
    fun getTotalCaloriesBurned() = runDAO.getTotalCaloriesBurned()
}
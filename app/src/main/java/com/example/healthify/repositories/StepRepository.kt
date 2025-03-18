package com.example.healthify.repositories

import com.example.healthify.db.StepDAO
import com.example.healthify.db.StepEntry
import javax.inject.Inject

class StepRepository @Inject constructor(private val stepDao: StepDAO) {

    suspend fun insertStep(stepEntry: StepEntry) = stepDao.insertStep(stepEntry)

    fun getAllSteps() = stepDao.getAllSteps()

    fun getTotalSteps() = stepDao.getTotalSteps()
}

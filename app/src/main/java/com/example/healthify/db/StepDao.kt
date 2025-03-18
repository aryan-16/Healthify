package com.example.healthify.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StepDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(stepEntry: StepEntry)

    @Query("SELECT * FROM step_table ORDER BY timestamp DESC")
    fun getAllSteps(): LiveData<List<StepEntry>>

    @Query("SELECT SUM(stepCount) FROM step_table")
    fun getTotalSteps(): LiveData<Int>

    @Query("SELECT * FROM step_table WHERE timestamp BETWEEN :startTime AND :endTime")
    fun getStepsBetween(startTime: Long, endTime: Long): LiveData<List<StepEntry>>
}

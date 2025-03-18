package com.example.healthify.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_table")
data class StepEntry(
    val timestamp: Long,
    val stepCount: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

package com.example.healthify.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Run::class, StepEntry::class], version = 2, exportSchema = false)
@TypeConverters(Convertors::class)
abstract class RunningDatabase : RoomDatabase() {

    abstract fun getRunDao(): RunDAO
    abstract fun getStepDao(): StepDAO
}

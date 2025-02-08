package com.example.healthify.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [Run::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Convertors::class)
abstract class RunningDatabase : RoomDatabase() {

    abstract fun getRunDao() : RunDAO
}
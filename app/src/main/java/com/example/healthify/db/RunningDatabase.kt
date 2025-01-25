package com.example.healthify.db

import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [Run::class],
    version = 1
)
@TypeConverters(Convertors::class)
abstract class RunningDatabase : RoomDatabase() {

    abstract fun getRunDao() : Dao
}
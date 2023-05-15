package com.example.todoplanner.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoplanner.adapters.RoomUserNameDataClass

@Database(entities = [RoomUserNameDataClass::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usernameDao() : UsernameDao
}
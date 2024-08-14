package com.example.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [SavedAccountEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun savedAccountDao(): SavedAccountDao
}
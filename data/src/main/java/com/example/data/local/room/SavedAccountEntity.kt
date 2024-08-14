package com.example.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_account")
data class SavedAccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo ("ownerName") val ownerName : String,
    @ColumnInfo ("accountNumber") val accountNumber : String,
    @ColumnInfo ("savedBy") val savedBy : String //User ID

)

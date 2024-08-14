package com.example.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface SavedAccountDao {
    @Query("SELECT * FROM saved_account WHERE savedBy = :userId")
    suspend fun showSavedAccounts(userId: String): List<SavedAccountEntity>

    @Insert
    suspend fun insertSavedAccount(savedAccount: SavedAccountEntity)

}
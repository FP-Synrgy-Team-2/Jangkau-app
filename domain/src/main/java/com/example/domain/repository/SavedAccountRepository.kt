package com.example.domain.repository

import com.example.domain.model.SavedAccount

interface SavedAccountRepository {

    suspend fun getSavedAccount(accountNumber : String) : List<SavedAccount>

}
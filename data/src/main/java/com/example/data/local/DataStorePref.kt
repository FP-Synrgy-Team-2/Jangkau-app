package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

//data Store preference
const val USER_PREF = "userJangkau"
class DataStorePref(private val context: Context) {
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(USER_PREF)

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val USER_ID = stringPreferencesKey("user_id")
        val TOKEN_TYPE = stringPreferencesKey("token_type")
    }

    fun storeLoginData(accessToken: String, userId: String, tokenType: String): Flow<Boolean> = flow {
        try {
            context.dataStore.edit { preference ->
                preference[ACCESS_TOKEN_KEY] = accessToken
                preference[USER_ID] = userId
                preference[TOKEN_TYPE] = tokenType
            }
            emit(true) // Emit true if the operation is successful
        } catch (e: Exception) {
            // Log the error or handle it as needed
            emit(false) // Emit false if an error occurs
        }
    }.catch { e ->
        // This block handles errors from the flow pipeline
        emit(false) // Emit false if an error occurs during flow operations
    }


    val userId : Flow<String?> = context.dataStore.data
        .map {
            preferences ->
            preferences[USER_ID]
        }

    val accessToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }

    val tokenType : Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[TOKEN_TYPE]
        }





}
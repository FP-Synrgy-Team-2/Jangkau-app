package com.example.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "user_prefs")


class DataStorePref(private val context: Context) {

    companion object {
        private const val TAG = "DataStorePref"
        val ACCESS_TOKEN_KEY = stringPreferencesKey("ACCESS_TOKEN")
        val USER_ID_KEY = stringPreferencesKey("USER_ID")
        val TOKEN_TYPE_KEY = stringPreferencesKey("TOKEN_TYPE")
    }

    val accessToken: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }
        .catch { e ->
            Log.e(TAG, "Error fetching access token", e)
            emit(null)
        }

    val userId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ID_KEY]
        }
        .catch { e ->
            Log.e(TAG, "Error fetching user ID", e)
            emit(null)
        }

    val tokenType: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[TOKEN_TYPE_KEY]
        }
        .catch { e ->
            Log.e(TAG, "Error fetching token type", e)
            emit(null)
        }

    suspend fun storeLoginData(accessToken: String, userId: String, tokenType: String): Flow<Boolean> = flow {
        try {
            Log.d(TAG, "Storing login data: userId = $userId, accessToken = $accessToken, tokenType = $tokenType")
            context.dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN_KEY] = accessToken
                preferences[USER_ID_KEY] = userId
                preferences[TOKEN_TYPE_KEY] = tokenType
            }
            Log.d(TAG, "Login data stored successfully")
            emit(true)
        } catch (e: Exception) {
            Log.e(TAG, "Error storing login data", e)
            emit(false)
        }
    }.catch { e ->
        Log.e(TAG, "Error in flow while storing login data", e)
        emit(false)
    }

    suspend fun storeUserAccountData(
        accountNumber : String,
        accountId : String
        ): Flow<Boolean> = flow {

    }



}

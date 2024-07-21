package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

//data Store preference
const val USER_PREF = "userJangkau"
class DataStorePref(private val context: Context) {
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(USER_PREF)



    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val USER_ID = stringPreferencesKey("user_id")
    private val TOKEN_TYPE = stringPreferencesKey("token_type")
    private val IS_LOGIN_KEY = stringPreferencesKey("is_login_key")

    suspend fun isLogin(): String = context.dataStore.data.first()[IS_LOGIN_KEY] ?: ""

    fun storeLoginData(accessToken : String, userId : String, tokenType : String) : Flow<Boolean> = flow {
        try {
            context.dataStore.edit {preference ->
                preference[ACCESS_TOKEN_KEY] = accessToken
                preference[USER_ID] = userId
                preference[TOKEN_TYPE] = tokenType
            }
            if (isLogin().isNotEmpty())
                emit(true)
            else
                throw Exception()
        } catch (e: Exception){
            emit(false)
        }
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
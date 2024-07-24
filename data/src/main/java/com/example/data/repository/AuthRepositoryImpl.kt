package com.example.data.repository

import android.util.Log
import com.example.data.local.DataStorePref
import com.example.data.network.ApiService
import com.example.data.network.model.auth.AuthRequest
import com.example.data.network.utils.SafeApiRequest
import com.example.domain.model.Auth
import com.example.domain.model.Login
import com.example.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val dataStorePref: DataStorePref
) : AuthRepository, SafeApiRequest() {

    override suspend fun login(auth: Auth): Login {
        val response = safeApiRequest {
            apiService.loginAuth(
                authRequest = AuthRequest(auth.username, auth.password)
            )
        }

        val loginResponse = response.data ?: throw Exception(response.message)

        Log.d("AuthRepositoryImpl", "Storing login data")
        dataStorePref.storeLoginData(
            accessToken = loginResponse.accessToken,
            userId = loginResponse.userId,
            tokenType = loginResponse.tokenType
        ).collect { success ->
            if (success) {
                Log.d("AuthRepositoryImpl", "Login data stored successfully")
            } else {
                Log.e("AuthRepositoryImpl", "Failed to store login data")
            }
        }

        return loginResponse.toDomain()
    }
}

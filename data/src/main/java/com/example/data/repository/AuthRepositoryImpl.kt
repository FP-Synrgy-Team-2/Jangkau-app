package com.example.data.repository

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
        val authRequest = AuthRequest(auth.username, auth.password)
        val response = safeApiRequest {
            apiService.loginAuth(authRequest)
        }

        // Check for success status
        if (response.status) {
            val loginData = response.data ?: throw Exception("Login data is null")
            dataStorePref.storeLoginData(
                loginData.accessToken,
                loginData.tokenType,
                loginData.userId
            )
            return Login(
                userId = loginData.userId,
                accessToken = loginData.accessToken,
                tokenType = loginData.tokenType,
            )
        } else {
            throw Exception(response.message)
        }
    }

}

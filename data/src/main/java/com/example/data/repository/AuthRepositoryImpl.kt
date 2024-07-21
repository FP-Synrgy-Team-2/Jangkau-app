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
        val response = safeApiRequest {
            apiService.loginAuth(
                authRequest = AuthRequest(auth.username, auth.password)
            )
        }

        val loginResponse = response.data ?: throw Exception(response.message)

        dataStorePref.storeLoginData(
            accessToken = loginResponse.accessToken,
            userId = loginResponse.userId,
            tokenType = loginResponse.tokenType
        )

        return loginResponse.toDomain()
    }

}

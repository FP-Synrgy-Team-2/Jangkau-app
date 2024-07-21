package com.example.data.repository

import android.util.Log
import com.example.data.local.DataStorePref
import com.example.data.network.ApiService
import com.example.data.network.utils.SafeApiRequest
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val dataStorePref: DataStorePref
) : UserRepository, SafeApiRequest() {
    override suspend fun getUserById(): User {
        val token = dataStorePref.accessToken.firstOrNull()
        val userId = dataStorePref.userId.firstOrNull()

        if (token == null) {
            throw Exception("Token not found")
        } else {
            Log.d("UserRepositoryImpl", "Token found: $token")
        }

        if (userId == null) {
            throw Exception("User ID not found")
        } else {
            Log.d("UserRepositoryImpl", "User ID found: $userId")
        }

        val response = safeApiRequest {
            apiService.getUser(userId, "Bearer $token")
        }

        val userResponse = response.data ?: throw Exception("User not found")
        return User(
            userId = userResponse.userId,
            phoneNumber = userResponse.phoneNumber,
            email = userResponse.emailAddress,
            fullname = userResponse.fullname
        )
    }
}

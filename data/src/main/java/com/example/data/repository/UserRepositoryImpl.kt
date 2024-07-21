package com.example.data.repository

import com.example.data.local.DataStorePref
import com.example.data.network.ApiService
import com.example.data.network.utils.SafeApiRequest
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull

class UserRepositoryImpl(
    private val apiService: ApiService,
    private val dataStorePref: DataStorePref
) : UserRepository, SafeApiRequest(){
    override suspend fun getUserById(userId: String): User {
        val token = dataStorePref.accessToken.firstOrNull() ?: throw Exception("Token not found")
        val response = safeApiRequest {
            apiService.getUser(userId, "Bearer $token")
        }
        return response.toDomain()
    }


}
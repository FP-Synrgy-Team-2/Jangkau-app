package com.example.data.repository

import android.util.Log
import com.example.data.local.DataStorePref
import com.example.data.network.ApiService
import com.example.data.network.utils.SafeApiRequest
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull

class UserRepositoryImpl(
    apiService: ApiService,
    dataStorePref: DataStorePref
) : BaseRepository(apiService, dataStorePref), UserRepository {

    override suspend fun getUserById(): User {
        val userId = dataStorePref.userId.firstOrNull()

        if (userId == null) {
            throw Exception("User ID not found")
        } else {
            Log.d("UserRepositoryImpl", "User ID found: $userId")
        }

        val response = performRequestWithTokenHandling { token ->
            apiService.getUser(userId, token)
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

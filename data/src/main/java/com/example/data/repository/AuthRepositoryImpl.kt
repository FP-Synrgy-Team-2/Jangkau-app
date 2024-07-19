package com.example.data.repository

import com.example.domain.model.Auth
import com.example.domain.model.User
import com.example.domain.repository.AuthRepository

class AuthRepositoryImpl() : AuthRepository {
    override suspend fun login(auth: Auth): User? {
        return null
    }

}

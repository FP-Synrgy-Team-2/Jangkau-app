package com.example.domain.repository

import com.example.domain.model.Auth
import com.example.domain.model.User

interface AuthRepository {
    suspend fun login(auth : Auth) : User

}
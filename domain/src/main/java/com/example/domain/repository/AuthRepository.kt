package com.example.domain.repository

import com.example.domain.model.Auth
import com.example.domain.model.Login

interface AuthRepository {
    suspend fun login(auth : Auth) : Login

}
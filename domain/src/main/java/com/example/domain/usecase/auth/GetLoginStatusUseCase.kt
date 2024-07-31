package com.example.domain.usecase.auth

import com.example.common.Resource
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLoginStatusUseCase(private val authRepository: AuthRepository) {
    suspend fun isLoggedIn(): Boolean {
        return authRepository.getLoginStatus()
    }

}
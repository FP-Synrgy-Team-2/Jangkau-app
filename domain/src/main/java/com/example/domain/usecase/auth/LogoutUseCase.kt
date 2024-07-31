package com.example.domain.usecase.auth

import com.example.common.Resource
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LogoutUseCase(private val authRepository: AuthRepository) {

    suspend fun execute(): Flow<Boolean> = flow {
        try {
            authRepository.logout()
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }
}
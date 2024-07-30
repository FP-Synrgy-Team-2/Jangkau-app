package com.example.domain.usecase.auth

import com.example.common.Resource
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLoginStatusUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val isLoggedIn = authRepository.getLoginStatus()
            emit(Resource.Success(isLoggedIn))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }

}
package com.example.domain.usecase.auth

import com.example.common.Resource
import com.example.domain.model.Auth
import com.example.domain.model.User
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginUseCase(private val authRepository: AuthRepository) {

    // auth can be changed to email & password
    operator fun invoke(auth : Auth) : Flow<Resource<User?>> = flow {
        emit(Resource.Loading())
        try {
            val response = authRepository.login(auth)
            emit(Resource.Success(response))
        }catch (e: Exception){
            emit(Resource.Error("Error Occurred"))
        }
    }

}
package com.example.domain.usecase.auth

import android.util.Log
import com.example.common.Resource
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ForgotPasswordUseCase (private val authRepository: AuthRepository) {
    operator fun invoke(email : String) : Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val response = authRepository.forgotPassword(email)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Error("${e.message}"))
            Log.e("ForgotPasswordUseCase", e.message.toString()) // Log the exception
        }
    }
}
package com.example.domain.usecase.auth

import android.util.Log
import com.example.common.Resource
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ValidateOtpUseCase(private val authRepository: AuthRepository) {
    operator fun invoke(otp : String) : Flow<Resource<String>> = flow{
        emit(Resource.Loading())
        try {
            val respone = authRepository.validateOTP(otp)
            emit(Resource.Success(respone))
        }catch (e : Exception){
            emit(Resource.Error("${e.message}"))
            Log.e("ValidateOtpUseCase",e.message.toString())
        }
    }
}
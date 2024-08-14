package com.example.domain.usecase.auth

import android.util.Log
import com.example.common.Resource
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ResetPasswordUseCase (private val authRepository: AuthRepository) {
    operator fun invoke(email : String, otp: String, newPassword : String) : Flow<Resource<String>> = flow{
        emit(Resource.Loading())
        try {
            val response = authRepository.resetPassword(email,otp,newPassword)
            emit(Resource.Success(response))
        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
            Log.e("resetPassword",e.message.toString())
        }
    }
}
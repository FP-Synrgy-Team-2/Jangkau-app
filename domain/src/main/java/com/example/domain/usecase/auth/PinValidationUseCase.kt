package com.example.domain.usecase.auth

import android.util.Log
import com.example.common.Resource
import com.example.domain.model.BankAccount
import com.example.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PinValidationUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(pin : String) : Flow<Resource<BankAccount>> = flow {
        emit(Resource.Loading())
        try {
            val response = authRepository.pinValidation(pin)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error("Error Occurred: ${e.message}"))
            Log.e("PinValidationUseCase", "PinValidation Failed")
        }
    }
}
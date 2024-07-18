package com.example.domain.usecase.auth

import com.example.domain.model.Auth
import com.example.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {

    // auth can be changed to email & password
    suspend fun invoke(auth : Auth){

        /**
         *
         *
         *
         */

    }

}
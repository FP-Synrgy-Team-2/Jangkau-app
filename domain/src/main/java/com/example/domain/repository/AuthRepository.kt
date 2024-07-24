package com.example.domain.repository

import com.example.domain.model.Auth
import com.example.domain.model.BankAccount
import com.example.domain.model.Login
import com.example.domain.model.PinValidation

interface AuthRepository {
    suspend fun login(auth : Auth) : Login

    suspend fun pinValidation(pinValidation: PinValidation) : BankAccount

}
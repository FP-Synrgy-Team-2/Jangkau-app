package com.example.domain.repository

import com.example.domain.model.BankAccount
import com.example.domain.model.PinValidation

interface BankAccountRepository {

    suspend fun getBankAccountById(userId :String) : BankAccount

    suspend fun getBankAccountByAccountNumber(accountNumber : String) : BankAccount

    suspend fun getAllBankAccount() : BankAccount

    suspend fun getPinValidation(pinValidation: PinValidation) : BankAccount

}
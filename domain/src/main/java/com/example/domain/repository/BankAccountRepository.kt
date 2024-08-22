package com.example.domain.repository

import android.graphics.Bitmap
import com.example.domain.model.BankAccount
import com.example.domain.model.PinValidation

interface BankAccountRepository {

    suspend fun getBankAccountById() : BankAccount

    suspend fun getSavedBankAccount() : List<BankAccount>

    suspend fun getBankAccountByAccountNumber(accountNumber : String) : BankAccount

    suspend fun scanQr(encryptedData : String) : BankAccount

    suspend fun generateQr() : Bitmap
//
//    suspend fun getAllBankAccount() : BankAccount
//
//    suspend fun getPinValidation(pinValidation: PinValidation) : BankAccount

}
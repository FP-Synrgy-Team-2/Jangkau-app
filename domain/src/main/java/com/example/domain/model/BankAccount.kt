package com.example.domain.model

data class BankAccount(
    val accountId : String,
    val userId : String,
    val ownerName : String,
    val accountNumber : String,
    val balance : Double
)

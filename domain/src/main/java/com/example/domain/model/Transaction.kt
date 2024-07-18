package com.example.domain.model

data class Transaction(
    val accountId : String,
    val beneficiaryAccount : String,
    val amount : Int,
    val adminFee : Int,
    val transactionDate : String,
    val note : String,
    val total : Int
)

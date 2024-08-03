package com.example.domain.model

data class Transaction(
    val transactionId : String,
    val accountId : String,
    val beneficiaryAccount : String,
    val beneficiaryName : String,
    val beneficiaryAccountId : String,
    val amount : Int,
    val adminFee : Int,
    val transactionDate : String,
    val date : String,
    val note : String,
    val isSaved : Boolean?
)

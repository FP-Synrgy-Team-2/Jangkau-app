package com.example.domain.model

data class TransferRequest(
    val accountId : String,
    val beneficiaryAccount : String,
    val amount : Int,
    val transactionDate : String,
    val note : String,
    val isSaved : Boolean = false
    )

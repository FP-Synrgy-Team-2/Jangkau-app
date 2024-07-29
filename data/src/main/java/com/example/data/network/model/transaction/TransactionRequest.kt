package com.example.data.network.model.transaction

data class TransactionRequest(
    val accountId : String?,
    val beneficiaryAccount : String,
    val amount : Int,
    val transactionDate : String,
    val note : String,
    val isSaved : Boolean = false
    )

package com.example.domain.model

data class TransactionGroup(
    val date: String,
    val transactions: List<Transaction>
)

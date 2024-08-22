package com.example.domain.model

import java.io.Serializable

data class BankAccount(
    val accountId : String?,
    val userId : String? = "",
    val ownerName : String,
    val accountNumber : String,
    val balance : Double? = 0.0,
    val type : String? = null
) : Serializable

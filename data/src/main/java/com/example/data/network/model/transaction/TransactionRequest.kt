package com.example.data.network.model.transaction

import com.google.gson.annotations.SerializedName
data class TransactionRequest(
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("beneficiary_account")
    val beneficiaryAccount: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("saved")
    val saved: Boolean,
//    @SerializedName("transaction_date")
//    val transactionDate: String
)

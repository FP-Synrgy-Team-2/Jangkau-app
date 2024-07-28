package com.example.data.network.model.transaction


import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("admin_fee")
    val adminFee: Int,
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("beneficiary_account")
    val beneficiaryAccount: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("total")
    val total: Int,
    @SerializedName("transaction_date")
    val transactionDate: String,
    @SerializedName("transaction_id")
    val transactionId: String
)
package com.example.data.network.model


import com.google.gson.annotations.SerializedName


data class TransactionHistoryResponse(
    @SerializedName("from")
    val from: From,
    @SerializedName("to")
    val to: To,
    @SerializedName("total")
    val total: Double,
    @SerializedName("transaction_date")
    val transactionDate: String,
    @SerializedName("transaction_id")
    val transactionId: String,
    @SerializedName("type")
    val type: String
)

data class To(
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("account_number")
    val accountNumber: String,
    @SerializedName("owner_name")
    val ownerName: String
)

data class From(
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("account_number")
    val accountNumber: String,
    @SerializedName("owner_name")
    val ownerName: String
)
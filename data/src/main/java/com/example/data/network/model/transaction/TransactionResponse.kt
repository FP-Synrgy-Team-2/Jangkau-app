package com.example.data.network.model.transaction

import com.example.domain.model.Transaction
import com.google.gson.annotations.SerializedName

data class BeneficiaryAccount(
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("owner_name")
    val ownerName: String,
    @SerializedName("account_number")
    val accountNumber: String
)

data class TransactionResponse(
    @SerializedName("transaction_id")
    val transactionId: String,
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("beneficiary_account")
    val beneficiaryAccount: BeneficiaryAccount?,
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("transaction_date")
    val transactionDate: String? = "",
    @SerializedName("date")
    val date: String? = "",
    @SerializedName("note")
    val note: String,
    @SerializedName("admin_fee")
    val adminFee: Int,
    @SerializedName("total")
    val total: Int
)

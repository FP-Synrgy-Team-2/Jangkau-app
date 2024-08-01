package com.example.data.network.model.transaction


import com.example.domain.model.Transaction
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
) {
    fun toDomain(): Transaction {
        return Transaction(
            accountId = accountId,
            adminFee = adminFee,
            amount = amount,
            beneficiaryAccount = beneficiaryAccount,
            note = note,
            transactionDate = transactionDate,
            transactionId = transactionId,
            isSaved = null
        )
    }
}
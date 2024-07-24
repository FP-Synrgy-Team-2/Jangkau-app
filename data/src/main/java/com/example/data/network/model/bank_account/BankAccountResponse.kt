package com.example.data.network.model.bank_account


import com.example.domain.model.BankAccount
import com.google.gson.annotations.SerializedName

data class BankAccountResponse(
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("account_number")
    val accountNumber: String,
    @SerializedName("balance")
    val balance: Double,
    @SerializedName("owner_name")
    val ownerName: String,
    @SerializedName("user_id")
    val userId: String,
) {
    fun toDomain(): BankAccount {
        return BankAccount(
            accountId = accountId,
            accountNumber = accountNumber,
            balance = balance,
            ownerName = ownerName,
            userId = userId
        )
    }
}
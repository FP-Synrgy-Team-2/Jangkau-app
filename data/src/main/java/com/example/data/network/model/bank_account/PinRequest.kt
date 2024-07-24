package com.example.data.network.model.bank_account


import com.google.gson.annotations.SerializedName

data class PinRequest(
    @SerializedName("account_number")
    val accountNumber: String,
    @SerializedName("pin")
    val pin: String
)
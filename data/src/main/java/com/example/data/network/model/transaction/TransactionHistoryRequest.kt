package com.example.data.network.model.transaction


import com.google.gson.annotations.SerializedName

data class TransactionHistoryRequest(
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("start_date")
    val startDate: String
)
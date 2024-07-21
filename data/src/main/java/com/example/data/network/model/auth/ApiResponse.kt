package com.example.data.network.model.auth

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("data")
    val data: T?
)


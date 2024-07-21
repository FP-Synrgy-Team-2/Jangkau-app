package com.example.data.network.model.auth

import com.example.domain.model.Login
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    val code: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: Boolean,

    @SerializedName("data")
    val data: LoginData?
) {
    fun toDomain(): Login {
        // Ensure data is not null
        val loginData = data ?: throw Exception("Login data is null")
        return Login(
            userId = loginData.userId,
            accessToken = loginData.accessToken,
            tokenType = loginData.tokenType
        )
    }
}

data class LoginData(
    @SerializedName("user_id")
    val userId: String,

    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("expires_in")
    val expiresIn: Int,

    @SerializedName("scope")
    val scope: String,

    @SerializedName("jti")
    val jti: String
)

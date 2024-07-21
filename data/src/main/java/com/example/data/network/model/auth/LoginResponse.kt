package com.example.data.network.model.auth

import com.example.domain.model.Login
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("jti")
    val jti: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("token_type")
    val tokenType: String
) {
    fun toDomain(): Login {
        return Login(
            userId = userId,
            accessToken = accessToken,
            tokenType = tokenType
        )
    }
}

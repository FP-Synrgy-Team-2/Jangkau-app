package com.example.data.network.model.auth


import com.example.domain.model.User
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("email_address")
    val emailAddress: String,
    @SerializedName("full_name")
    val fullname: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("id")
    val userId: String,
    @SerializedName("username")
    val username: String
) {
    fun toDomain(): User {
        return User(
            email = emailAddress,
            fullname = fullname,
            phoneNumber = phoneNumber,
            userId = userId,
            username = username
        )
    }
}
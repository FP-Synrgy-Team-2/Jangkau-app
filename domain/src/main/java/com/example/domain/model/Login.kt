package com.example.domain.model

data class Login (
    val userId : String,
    val accessToken : String,
//    val refreshToken : String,
    val tokenType : String
)
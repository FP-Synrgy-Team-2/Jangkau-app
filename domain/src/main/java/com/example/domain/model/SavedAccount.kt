package com.example.domain.model

import java.io.Serializable

data class SavedAccount (
    val ownerName : String,
    val accountNumber : String,
) : Serializable
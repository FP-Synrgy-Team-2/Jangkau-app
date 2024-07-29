package com.example.data.network.utils

import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun <T : Any> safeApiRequest(call: suspend () -> Response<T>): Response<T> {
        return try {
            call.invoke()
        } catch (e: Exception) {
            throw Exception("Network request failed: ${e.message}")
        }
    }
}

package com.example.data.network.utils

import com.example.data.network.model.ApiResponse
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun <T : Any> safeApiRequest(call: suspend () -> Response<ApiResponse<T>>): ApiResponse<T> {
        val response = call.invoke()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Response body is null")
        } else {
            val errorResponse = response.errorBody()?.string()
            throw Exception(errorResponse)
        }
    }
}

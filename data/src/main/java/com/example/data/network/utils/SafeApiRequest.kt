package com.example.data.network.utils

import android.util.Log
import com.example.data.network.model.auth.ApiResponse
import org.json.JSONException
import retrofit2.Response
import java.io.IOException

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

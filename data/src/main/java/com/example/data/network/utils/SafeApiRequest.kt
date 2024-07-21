package com.example.data.network.utils

import android.util.Log
import org.json.JSONException
import retrofit2.Response
import java.io.IOException

abstract class SafeApiRequest {

    suspend fun <T : Any> safeApiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful) {
            response.body()?.let {
                return it
            } ?: throw ApiException("Response body is null")
        } else {
            val responseErr = response.errorBody()?.string()
            val message = StringBuilder()
            responseErr?.let {
                try {
                    message.append(it)
                } catch (e: JSONException) {
                    message.append(response.message())
                }
            }
            Log.d("TAG", "safeApiRequest: $message")
            throw ApiException(message.toString())
        }
    }
}

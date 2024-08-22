package com.example.data.repository

import android.util.Log
import com.example.data.local.DataStorePref
import com.example.data.network.ApiService
import com.example.data.network.model.ApiResponse
import com.example.data.network.utils.SafeApiRequest
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.Response

abstract class BaseRepository(
    protected val apiService: ApiService,
    protected val dataStorePref: DataStorePref
) : SafeApiRequest() {

    companion object {
        private const val TAG = "BaseRepository"
    }

    protected suspend fun refreshToken(): Boolean {
        val refreshToken = dataStorePref.refreshToken.firstOrNull() ?: run {
            Log.e(TAG, "Refresh token not found")
            return false
        }

        val response = safeApiRequest {
            apiService.refreshToken(
                grantType = "refresh_token",
                clientId = "my-client-web",
                clientSecret = "password",
                refreshToken = refreshToken
            )
        }

        if (!response.isSuccessful) {
            Log.e(TAG, "Failed to refresh token: ${response.errorBody()?.string()}")
            return false
        }

        val refreshResponse = response.body() ?: run {
            Log.e(TAG, "Refresh token response body is null")
            return false
        }

        Log.d(TAG, "New access token: ${refreshResponse.accessToken}")

        // Save new tokens in dataStorePref
        dataStorePref.updateAccessToken(refreshResponse.accessToken)
        dataStorePref.updateRefreshToken(refreshResponse.refreshToken)

        return true
    }

    protected suspend fun <T : Any> performRequestWithTokenHandlingWithoutApiResponse(
        request: suspend (String) -> Response<T>
    ): T {
        var token = dataStorePref.accessToken.firstOrNull() ?: throw Exception("Access token not found")
        var response = safeApiRequest { request("Bearer $token") }

        if (response.code() == 401) {
            val errorBody = response.errorBody()?.string()
            if (errorBody != null && errorBody.contains("invalid_token") && errorBody.contains("Access token expired")) {
                Log.d(TAG, "Access token expired, attempting to refresh")
                val tokenRefreshed = refreshToken()
                if (tokenRefreshed) {
                    token = dataStorePref.accessToken.firstOrNull() ?: throw Exception("Access token not found after refresh")
                    response = safeApiRequest { request("Bearer $token") }
                } else {
                    throw Exception("Failed to refresh token")
                }
            } else {
                throw Exception("Unauthorized request: ${response.message()}")
            }
        }

        if (response.code() == 500) {
            throw Exception("Server error, please try again later")
        }

        if (response.code() == 400) {
            val errorBody = response.errorBody()?.string()
            if (errorBody != null && errorBody.contains("Incorrect PIN")) {
                throw Exception("Incorrect PIN")
            }
        }

        return response.body() ?: throw Exception("No data found in response")
    }


    protected suspend fun <T : Any> performRequestWithTokenHandling(request: suspend (String) -> Response<ApiResponse<T>>): ApiResponse<T> {
        var token = dataStorePref.accessToken.firstOrNull() ?: throw Exception("Access token not found")
        var response = safeApiRequest { request("Bearer $token") }

        if (response.code() == 401) {
            val errorBody = response.errorBody()?.string()
            if (errorBody != null && errorBody.contains("invalid_token") && errorBody.contains("Access token expired")) {
                Log.d(TAG, "Access token expired, attempting to refresh")
                val tokenRefreshed = refreshToken()
                if (tokenRefreshed) {
                    token = dataStorePref.accessToken.firstOrNull() ?: throw Exception("Access token not found after refresh")
                    response = safeApiRequest { request("Bearer $token") }
                } else {
                    throw Exception("Failed to refresh token")
                }
            } else {
                throw Exception("Unauthorized request: ${response.message()}")
            }
        }

        if (response.code() == 500) {
            throw Exception("Server bermasalah, coba lagi nanti")
        }

        if (response.code() == 400) {
            val errorBody = response.errorBody()?.string()
            if (errorBody != null && errorBody.contains("Incorrect PIN")){
                throw Exception("PIN Salah")
            }
        }

        return response.body()!!
    }
}

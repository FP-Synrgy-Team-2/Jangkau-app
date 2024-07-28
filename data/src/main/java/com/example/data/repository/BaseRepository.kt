package com.example.data.repository

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

    protected suspend fun refreshToken(): Boolean {
        val refreshToken = dataStorePref.refreshToken.firstOrNull()
        val clientId = "my-client-web"
        val clientSecret = "password"

        if (refreshToken == null) {
            throw Exception("Refresh token not found")
        }

        val response = safeApiRequest {
            apiService.refreshToken(
                grantType = "refresh_token",
                clientId = clientId,
                clientSecret = clientSecret,
                refreshToken = refreshToken
            )
        }

        val refreshResponse = response.data ?: return false

        // Save new tokens in dataStorePref
        dataStorePref.updateAccessToken(refreshResponse.accessToken)
        dataStorePref.updateRefreshToken(refreshResponse.refreshToken)

        return true
    }

    protected suspend fun <T : Any> performRequestWithTokenHandling(request: suspend (String) -> Response<ApiResponse<T>>): ApiResponse<T> {
        var token = dataStorePref.accessToken.firstOrNull() ?: throw Exception("Access token not found")
        var response = safeApiRequest { request("Bearer $token") }

        // Check if the response is unauthorized (401) and attempt to refresh the token
        if (response.code == 401) {
            val tokenRefreshed = refreshToken()
            if (tokenRefreshed) {
                token = dataStorePref.accessToken.firstOrNull() ?: throw Exception("Access token not found after refresh")
                response = safeApiRequest { request("Bearer $token") }
            } else {
                throw Exception("Failed to refresh token")
            }
        }

        return response
    }
}

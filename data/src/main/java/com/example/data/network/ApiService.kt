package com.example.data.network

import com.example.data.network.model.ApiResponse
import com.example.data.network.model.auth.AuthRequest
import com.example.data.network.model.auth.LoginResponse
import com.example.data.network.model.auth.UserResponse
import com.example.data.network.model.bank_account.BankAccountResponse
import com.example.data.network.model.bank_account.PinRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("api/auth/login")
    suspend fun loginAuth(@Body authRequest: AuthRequest) : Response<ApiResponse<LoginResponse>>

    @GET("api/users/{id}")
    suspend fun getUser(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserResponse>>

    @GET("/api/bank-accounts/user/{id}")
    suspend fun getBankAccountById(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<BankAccountResponse>>

    @GET("/api/bank-accounts/account/{account_number}")
    suspend fun getBankAccountByAccountNumber(
        @Path("account_number") accountNumber: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<BankAccountResponse>>


    @GET("/api/saved-accounts/{id}")
    suspend fun getSavedBankAccount(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ) : Response<ApiResponse<List<BankAccountResponse>>>


    @POST("/api/bank-accounts/pin-validation")
    suspend fun pinValidation(
        @Body pinRequest : PinRequest,
        @Header("Authorization") token: String
    ) : Response<ApiResponse<BankAccountResponse>>

}
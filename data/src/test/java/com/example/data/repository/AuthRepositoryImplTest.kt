package com.example.data.repository

import com.example.data.local.DataStorePref
import com.example.data.network.ApiService
import com.example.data.network.model.ApiResponse
import com.example.data.network.model.auth.AuthRequest
import com.example.data.network.model.auth.LoginResponse
import com.example.domain.model.Auth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AuthRepositoryImplTest {

    private lateinit var service: ApiService
    private lateinit var dataStorePref: DataStorePref
    private lateinit var repository: AuthRepositoryImpl
    private lateinit var auth: Auth
    private lateinit var loginResponse: LoginResponse

    @Before
    fun setup() {
        service = mockk()
        dataStorePref = mockk()
        auth = Auth(username = "testuser", password = "testpass")
        loginResponse = LoginResponse(
            userId = "1",
            accountNumber = "someUid",
            accessToken = "testToken",
            refreshToken = "testRefreshToken",
            tokenType = "Bearer",
            expiresIn = 3600,
            jti = "testJti",
            scope = "testScope"
        )

        coEvery {
            dataStorePref.storeLoginData(
                accessToken = "testToken",
                userId = "1",
                tokenType = "Bearer",
                refreshToken = "testRefreshToken"
            )
        } returns flowOf(true) // or flow { emit(true) }

        repository = AuthRepositoryImpl(service, dataStorePref)
    }

    @Test
    fun login() = runBlocking {

        val apiResponse = ApiResponse(
            code = 200,
            message = "Success",
            status = true,
            data = loginResponse,
            error = ""
        )
        val response = Response.success(apiResponse)

        coEvery {
            service.loginAuth(AuthRequest(username = auth.username, password = auth.password))
        } returns response

        coEvery {
            dataStorePref.storeLoginData(
                accessToken = "testToken",
                userId = "1",
                tokenType = "Bearer",
                refreshToken = "testRefreshToken"
            )
        } returns flowOf(true)

        val result = repository.login(auth)

        assertEquals(loginResponse.toDomain(), result)
    }

}
package com.example.data.repository

import com.example.data.local.DataStorePref
import com.example.data.network.ApiService
import com.example.data.network.model.ApiResponse
import com.example.data.network.model.auth.AuthRequest
import com.example.data.network.model.auth.LoginResponse
import com.example.data.network.model.bank_account.BankAccountResponse
import com.example.domain.model.Auth
import com.example.domain.model.BankAccount
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.firstOrNull
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
    private lateinit var bankResponse : BankAccountResponse

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

        bankResponse = BankAccountResponse(
            accountId = "1",
            accountNumber = "someUid",
            ownerName = "John Doe",
            balance = 10000.0,
            userId = "someUid"
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

        coVerify {
            dataStorePref.storeLoginData(
                accessToken = "testToken",
                userId = "1",
                tokenType = "Bearer",
                refreshToken = "testRefreshToken"
            )
        }
    }



    @Test
    fun pinValidation_success() = runBlocking {
        val accountNumber = "1234567890"
        val pin = "1234"
        val bankAccount = BankAccount(
            accountId = "1",
            accountNumber = accountNumber,
            ownerName = "John Doe",
            balance = 10000.0,
            userId = null
        )

        val pinValidationResponse = BankAccountResponse(
            accountId = "1",
            accountNumber = accountNumber,
            ownerName = "John Doe",
            balance = 10000.0,
            userId = "someUid"
        )

        val apiResponse = ApiResponse(
            code = 200,
            message = "Success",
            status = true,
            data = pinValidationResponse,
            error = ""
        )

        coEvery { dataStorePref.accountNumber.firstOrNull() } returns accountNumber
        coEvery { service.pinValidation(any(), any()) } returns Response.success(apiResponse)

        val result = repository.pinValidation(pin)

        assertEquals(bankAccount, result)
        coVerify { service.pinValidation(any(), any()) }
    }

    @Test(expected = Exception::class)
    fun pinValidation_accountNumberNotFound(): Unit = runBlocking {
        coEvery { dataStorePref.accountNumber.firstOrNull() } returns null

        repository.pinValidation("1234")
    }

    @Test(expected = Exception::class)
    fun pinValidation_errorResponse(): Unit = runBlocking {
        val accountNumber = "1234567890"
        val pin = "1234"

        val apiResponse = ApiResponse<BankAccountResponse>(
            code = 400,
            message = "Invalid PIN",
            status = false,
            data = null,
            error = ""
        )

        coEvery { dataStorePref.accountNumber.firstOrNull() } returns accountNumber
        coEvery { service.pinValidation(any(), any()) } returns Response.success(apiResponse)

        repository.pinValidation(pin)
    }

    @Test
    fun resetPassword_success() = runBlocking {
        val email = "test@example.com"
        val otp = "123456"
        val newPassword = "newPassword123"
        val successMessage = "Password reset successfully"

        val apiResponse = ApiResponse(
            code = 200,
            message = "Success",
            status = true,
            data = successMessage,
            error = ""
        )

        coEvery { service.resetPassword(any()) } returns Response.success(apiResponse)

        val result = repository.resetPassword(email, otp, newPassword)

        assertEquals(successMessage, result)
        coVerify { service.resetPassword(any()) }
    }

    @Test(expected = Exception::class)
    fun resetPassword_invalidOtp(): Unit = runBlocking {
        val email = "test@example.com"
        val otp = "wrongOtp"
        val newPassword = "newPassword123"

        coEvery { service.resetPassword(any()) } returns Response.error(400, mockk())

        repository.resetPassword(email, otp, newPassword)
    }

    @Test(expected = Exception::class)
    fun resetPassword_emailNotFound(): Unit = runBlocking {
        val email = "notfound@example.com"
        val otp = "123456"
        val newPassword = "newPassword123"

        coEvery { service.resetPassword(any()) } returns Response.error(404, mockk())

        repository.resetPassword(email, otp, newPassword)
    }

    @Test(expected = Exception::class)
    fun resetPassword_serverError(): Unit = runBlocking {
        val email = "test@example.com"
        val otp = "123456"
        val newPassword = "newPassword123"

        coEvery { service.resetPassword(any()) } returns Response.error(500, mockk())

        repository.resetPassword(email, otp, newPassword)
    }




}
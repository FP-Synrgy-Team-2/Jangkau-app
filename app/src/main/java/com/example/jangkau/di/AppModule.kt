package com.example.jangkau.di

import com.example.data.local.DataStorePref
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.BankAccountRepositoryImpl
import com.example.data.repository.TransactionRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.BankAccountRepository
import com.example.domain.repository.TransactionRepository
import com.example.domain.repository.UserRepository
import com.example.domain.usecase.auth.ForgotPasswordUseCase
import com.example.domain.usecase.auth.GetLoginStatusUseCase
import com.example.domain.usecase.auth.LoginUseCase
import com.example.domain.usecase.auth.LogoutUseCase
import com.example.domain.usecase.auth.PinValidationUseCase
import com.example.domain.usecase.auth.ResetPasswordUseCase
import com.example.domain.usecase.auth.ValidateOtpUseCase
import com.example.domain.usecase.bank_account.SearchDataBankByAccNumberUseCase
import com.example.domain.usecase.bank_account.ShowDataBankAccUseCase
import com.example.domain.usecase.bank_account.ShowSavedBankAccUseCase
import com.example.domain.usecase.transaction.GetTransactionByIdUseCase
import com.example.domain.usecase.transaction.GetTransactionHistoryUseCase
import com.example.domain.usecase.transaction.TransferRequestUseCase
import com.example.domain.usecase.user.GetUserUseCase
import com.example.jangkau.viewmodel.AuthViewModel
import com.example.jangkau.viewmodel.UserViewModel
import com.example.jangkau.viewmodel.BankAccountViewModel
import com.example.jangkau.viewmodel.TransactionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single { DataStorePref(get()) }
    }

    val repositoryModule = module {
        factory<AuthRepository> { AuthRepositoryImpl(get(), get()) }
        factory<UserRepository> { UserRepositoryImpl(get(), get()) }
        factory<BankAccountRepository> { BankAccountRepositoryImpl(get(), get()) }
        factory<TransactionRepository> {TransactionRepositoryImpl(get(), get())}

    }

    val viewModelModule = module {
        viewModel { AuthViewModel(get(), get(), get(), get(), get(), get(), get()) }
        viewModel { UserViewModel(get()) }
        viewModel { BankAccountViewModel(get(), get(), get()) }
        viewModel {TransactionViewModel(get(), get(), get())}
    }

    val useCaseModule = module {
        factory { LoginUseCase(get()) }
        factory { PinValidationUseCase(get()) }
        factory { LogoutUseCase(get()) }
        factory { GetLoginStatusUseCase(get()) }
        factory { ForgotPasswordUseCase(get()) }
        factory { ValidateOtpUseCase(get()) }
        factory { ResetPasswordUseCase(get()) }

        factory { GetUserUseCase(get()) }

        factory { ShowDataBankAccUseCase(get()) }
        factory { ShowSavedBankAccUseCase(get()) }

        factory { SearchDataBankByAccNumberUseCase(get()) }

        factory { TransferRequestUseCase(get()) }
        factory { GetTransactionByIdUseCase(get()) }
        factory { GetTransactionHistoryUseCase(get()) }
    }

}
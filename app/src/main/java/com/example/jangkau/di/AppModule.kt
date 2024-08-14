package com.example.jangkau.di

import android.app.Application
import androidx.room.Room
import com.example.data.local.DataStorePref
import com.example.data.local.room.AppDatabase
import com.example.data.repository.*
import com.example.domain.repository.*
import com.example.domain.usecase.auth.*
import com.example.domain.usecase.bank_account.*
import com.example.domain.usecase.transaction.*
import com.example.domain.usecase.user.GetUserUseCase
import com.example.jangkau.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single { DataStorePref(get()) }

        single {
            Room.databaseBuilder(
                get<Application>(),
                AppDatabase::class.java,
                "app_database"
            ).build()
        }

        // Provide SavedAccountDao
        single { get<AppDatabase>().savedAccountDao() }
    }

    val repositoryModule = module {
        factory<AuthRepository> { AuthRepositoryImpl(get(), get()) }
        factory<UserRepository> { UserRepositoryImpl(get(), get()) }
        factory<BankAccountRepository> { BankAccountRepositoryImpl(get(), get(), get()) }
        factory<TransactionRepository> {TransactionRepositoryImpl(get(), get(), get())}

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
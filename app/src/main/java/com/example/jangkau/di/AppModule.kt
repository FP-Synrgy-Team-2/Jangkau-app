package com.example.jangkau.di

import com.example.data.local.DataStorePref
import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.UserRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.UserRepository
import com.example.domain.usecase.auth.LoginUseCase
import com.example.domain.usecase.user.GetUserUseCase
import com.example.jangkau.feature.auth.AuthViewModel
import com.example.jangkau.feature.auth.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single { DataStorePref(get()) }
    }

    val repositoryModule = module {
        factory<AuthRepository> { AuthRepositoryImpl(get(), get()) }
        factory<UserRepository> { UserRepositoryImpl(get(), get()) }
    }

    val viewModelModule = module {
        viewModel { AuthViewModel(get()) }
        viewModel { UserViewModel(get()) }
    }

    val useCaseModule = module {
        factory { LoginUseCase(get()) }

        factory { GetUserUseCase(get()) }
    }

}
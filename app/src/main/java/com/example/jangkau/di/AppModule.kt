package com.example.jangkau.di

import com.example.data.repository.AuthRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.usecase.auth.LoginUseCase
import com.example.jangkau.feature.auth.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single {  }
    }

    val repositoryModule = module {
        factory<AuthRepository> { AuthRepositoryImpl() }
    }

    val viewModelModule = module {
        viewModel { AuthViewModel(get()) }
    }

    val useCaseModule = module {
        factory { LoginUseCase(get()) }
    }

}
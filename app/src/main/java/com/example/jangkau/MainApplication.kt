package com.example.jangkau

import android.app.Application
import com.example.jangkau.di.AppModule.appModule
import com.example.jangkau.di.AppModule.repositoryModule
import com.example.jangkau.di.AppModule.useCaseModule
import com.example.jangkau.di.AppModule.viewModelModule
import com.example.jangkau.di.NetworkModule.networkModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()


        FirebaseApp.initializeApp(this)
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules( networkModule, appModule, repositoryModule, viewModelModule, useCaseModule)
        }

    }
}
package com.example.wherenow

import android.app.Application
import com.example.wherenow.di.databaseModule
import com.example.wherenow.di.networkModule
import com.example.wherenow.di.repositoryModule
import com.example.wherenow.di.useCaseModule
import com.example.wherenow.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WhereNowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@WhereNowApp)
            modules(
                useCaseModule,
                networkModule,
                databaseModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}

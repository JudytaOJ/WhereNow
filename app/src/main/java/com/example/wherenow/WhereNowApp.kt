package com.example.wherenow

import android.app.Application
import com.example.wherenow.di.whereNowAppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WhereNowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(MainActivity())
            modules(whereNowAppModule)
        }
    }
}
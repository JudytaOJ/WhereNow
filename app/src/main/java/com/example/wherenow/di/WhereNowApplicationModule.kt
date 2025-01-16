package com.example.wherenow.di

import android.app.Application
import androidx.room.Room
import com.airbnb.lottie.BuildConfig
import com.example.wherenow.data.Const.BASE_URL
import com.example.wherenow.data.network.WhereNowApi
import com.example.wherenow.data.network.WhereNowApiService
import com.example.wherenow.data.network.WhereNowApiServiceImpl
import com.example.wherenow.database.TripDao
import com.example.wherenow.database.TripDatabase
import com.example.wherenow.repository.TripCityRepository
import com.example.wherenow.repository.TripCityRepositoryImpl
import com.example.wherenow.repository.TripListRepository
import com.example.wherenow.repository.TripListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WhereNowApplicationModule {

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient() =
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        } else {
            OkHttpClient
                .Builder()
                .build()
        }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): WhereNowApi = retrofit.create(WhereNowApi::class.java)

    @Provides
    @Singleton
    fun provideApiWhereNowService(api: WhereNowApiServiceImpl): WhereNowApiService = api

    @Provides
    @Singleton
    fun provideTripListRepositoryImpl(repository: TripListRepositoryImpl): TripListRepository = repository

    @Provides
    @Singleton
    fun provideTripCityRepositoryImpl(repository: TripCityRepositoryImpl): TripCityRepository = repository

    @Provides
    @Singleton
    fun myDatabase(application: Application): TripDatabase {
        return Room
            .databaseBuilder(application, TripDatabase::class.java, "my.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun myDao(database: TripDatabase): TripDao = database.dao()
}
package com.example.wherenow.di

import com.example.wherenow.data.network.WhereNowApiService
import com.example.wherenow.data.network.WhereNowApiServiceImpl
import com.example.wherenow.repository.TripCityRepository
import com.example.wherenow.repository.TripCityRepositoryImpl
import com.example.wherenow.repository.TripListRepository
import com.example.wherenow.repository.TripListRepositoryImpl
import com.example.wherenow.repository.importantnotes.ImportantNotesRepository
import com.example.wherenow.repository.importantnotes.ImportantNotesRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    //Repository and services
    singleOf(::TripCityRepositoryImpl) { bind<TripCityRepository>() }
    singleOf(::TripListRepositoryImpl) { bind<TripListRepository>() }
    singleOf(::ImportantNotesRepositoryImpl) { bind<ImportantNotesRepository>() }
    singleOf(::WhereNowApiServiceImpl) { bind<WhereNowApiService>() }
}
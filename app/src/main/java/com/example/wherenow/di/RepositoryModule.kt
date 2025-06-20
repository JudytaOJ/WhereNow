package com.example.wherenow.di

import com.example.wherenow.data.network.WhereNowApiService
import com.example.wherenow.data.network.WhereNowApiServiceImpl
import com.example.wherenow.data.resolver.AndroidFileNameResolver
import com.example.wherenow.database.file.domain.FileNameResolver
import com.example.wherenow.notification.TravelReminderService
import com.example.wherenow.notification.TravelReminderServiceImpl
import com.example.wherenow.repository.TripCityRepository
import com.example.wherenow.repository.TripCityRepositoryImpl
import com.example.wherenow.repository.TripListRepository
import com.example.wherenow.repository.TripListRepositoryImpl
import com.example.wherenow.repository.file.FileRepository
import com.example.wherenow.repository.file.FileRepositoryImpl
import com.example.wherenow.repository.importantnotes.ImportantNotesRepository
import com.example.wherenow.repository.importantnotes.ImportantNotesRepositoryImpl
import com.example.wherenow.repository.statesvisited.StatesVisitedDataStore
import com.example.wherenow.repository.statesvisited.StatesVisitedRepository
import com.example.wherenow.repository.statesvisited.StatesVisitedRepositoryImpl
import com.example.wherenow.repository.theme.ThemeDataStore
import com.example.wherenow.repository.theme.ThemeRepository
import com.example.wherenow.repository.theme.ThemeRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    //Repository and services
    singleOf(::TripCityRepositoryImpl) { bind<TripCityRepository>() }
    singleOf(::TripListRepositoryImpl) { bind<TripListRepository>() }
    singleOf(::ImportantNotesRepositoryImpl) { bind<ImportantNotesRepository>() }
    singleOf(::WhereNowApiServiceImpl) { bind<WhereNowApiService>() }
    singleOf(::StatesVisitedRepositoryImpl) { bind<StatesVisitedRepository>() }
    singleOf(::StatesVisitedDataStore)
    singleOf(::FileRepositoryImpl) { bind<FileRepository>() }
    singleOf(::TravelReminderServiceImpl) { bind<TravelReminderService>() }
    singleOf(::ThemeDataStore)
    singleOf(::ThemeRepositoryImpl) { bind<ThemeRepository>() }

    single<FileNameResolver> { AndroidFileNameResolver(get()) }
}
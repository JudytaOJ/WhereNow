package com.example.wherenow.di

import com.example.wherenow.data.network.WhereNowApiService
import com.example.wherenow.data.network.WhereNowApiServiceImpl
import com.example.wherenow.data.usecases.DeleteImportantNoteUseCase
import com.example.wherenow.data.usecases.DeleteTileOnListUseCase
import com.example.wherenow.data.usecases.GetActuallyTripListUseCase
import com.example.wherenow.data.usecases.GetAirportUseCase
import com.example.wherenow.data.usecases.GetCityListFromRepositoryUseCase
import com.example.wherenow.data.usecases.GetDistanceBetweenAirportUseCase
import com.example.wherenow.data.usecases.GetFutureTripListUseCase
import com.example.wherenow.data.usecases.GetImportantNotesListUseCase
import com.example.wherenow.data.usecases.GetListDataTileUseCase
import com.example.wherenow.data.usecases.GetPastTripListUseCase
import com.example.wherenow.data.usecases.SaveCityListUseCase
import com.example.wherenow.data.usecases.SaveDataTileUseCase
import com.example.wherenow.data.usecases.SaveImportantNoteUseCase
import com.example.wherenow.data.usecases.UpdateImportantNoteUseCase
import com.example.wherenow.repository.TripCityRepository
import com.example.wherenow.repository.TripCityRepositoryImpl
import com.example.wherenow.repository.TripListRepository
import com.example.wherenow.repository.TripListRepositoryImpl
import com.example.wherenow.repository.importantnotes.ImportantNotesRepository
import com.example.wherenow.repository.importantnotes.ImportantNotesRepositoryImpl
import com.example.wherenow.ui.app.tripdatadetails.TripDataDetailsViewModel
import com.example.wherenow.ui.app.triplist.TripListViewModel
import com.example.wherenow.ui.app.triptiledetails.TripTileDetailsViewModel
import com.example.wherenow.ui.app.triptiledetails.importantnotes.ImportantNotesViewModel
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.BlankNoteViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val whereNowAppModule = module {
    //Repository and services
    singleOf(::TripCityRepositoryImpl) { bind<TripCityRepository>() }
    singleOf(::TripListRepositoryImpl) { bind<TripListRepository>() }
    factoryOf(::ImportantNotesRepositoryImpl) { bind<ImportantNotesRepository>() }
    factoryOf(::WhereNowApiServiceImpl) { bind<WhereNowApiService>() }

    //View models
    viewModelOf(::TripListViewModel)
    viewModelOf(::TripDataDetailsViewModel)
    viewModelOf(::TripTileDetailsViewModel)
    viewModelOf(::ImportantNotesViewModel)
    viewModelOf(::BlankNoteViewModel)

    //Use cases
    factoryOf(::DeleteImportantNoteUseCase)
    factoryOf(::DeleteTileOnListUseCase)
    factoryOf(::GetActuallyTripListUseCase)
    factoryOf(::GetAirportUseCase)
    factoryOf(::GetCityListFromRepositoryUseCase)
    factoryOf(::GetDistanceBetweenAirportUseCase)
    factoryOf(::GetFutureTripListUseCase)
    factoryOf(::GetImportantNotesListUseCase)
    factoryOf(::GetListDataTileUseCase)
    factoryOf(::GetPastTripListUseCase)
    factoryOf(::SaveCityListUseCase)
    factoryOf(::SaveDataTileUseCase)
    factoryOf(::SaveImportantNoteUseCase)
    factoryOf(::UpdateImportantNoteUseCase)
}
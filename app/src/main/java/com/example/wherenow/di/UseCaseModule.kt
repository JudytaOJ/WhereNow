package com.example.wherenow.di

import com.example.wherenow.data.usecases.CancelPushUseCase
import com.example.wherenow.data.usecases.DeleteFileUseCase
import com.example.wherenow.data.usecases.DeleteImportantNoteUseCase
import com.example.wherenow.data.usecases.DeleteTileOnListUseCase
import com.example.wherenow.data.usecases.GetActuallyTripListUseCase
import com.example.wherenow.data.usecases.GetAirportUseCase
import com.example.wherenow.data.usecases.GetCityListFromRepositoryUseCase
import com.example.wherenow.data.usecases.GetDistanceBetweenAirportUseCase
import com.example.wherenow.data.usecases.GetFileNameUseCase
import com.example.wherenow.data.usecases.GetFilesListUseCase
import com.example.wherenow.data.usecases.GetFutureTripListUseCase
import com.example.wherenow.data.usecases.GetImportantNotesListUseCase
import com.example.wherenow.data.usecases.GetListDataTileUseCase
import com.example.wherenow.data.usecases.GetPastTripListUseCase
import com.example.wherenow.data.usecases.GetStatesVisitedUseCase
import com.example.wherenow.data.usecases.GetThemeModeUseCase
import com.example.wherenow.data.usecases.SaveCityListUseCase
import com.example.wherenow.data.usecases.SaveDataTileUseCase
import com.example.wherenow.data.usecases.SaveFileUseCase
import com.example.wherenow.data.usecases.SaveImportantNoteUseCase
import com.example.wherenow.data.usecases.SaveStatesVisitedUseCase
import com.example.wherenow.data.usecases.SendPushUseCase
import com.example.wherenow.data.usecases.SetThemeModeUseCase
import com.example.wherenow.data.usecases.UpdateImportantNoteUseCase
import com.example.wherenow.notification.NotificationScheduler
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
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
    factoryOf(::SaveStatesVisitedUseCase)
    factoryOf(::GetStatesVisitedUseCase)
    factoryOf(::GetFilesListUseCase)
    factoryOf(::SaveFileUseCase)
    factoryOf(::DeleteFileUseCase)
    factoryOf(::GetFileNameUseCase)
    factoryOf(::SendPushUseCase)
    factoryOf(::NotificationScheduler)
    factoryOf(::CancelPushUseCase)
    factoryOf(::GetThemeModeUseCase)
    factoryOf(::SetThemeModeUseCase)
}
package com.example.wherenow.di

import com.example.wherenow.ui.app.settingsmenu.appTheme.AppThemeViewModel
import com.example.wherenow.ui.app.settingsmenu.statesvisited.StatedVisitedViewModel
import com.example.wherenow.ui.app.tripdatadetails.TripDataDetailsViewModel
import com.example.wherenow.ui.app.triplist.TripListViewModel
import com.example.wherenow.ui.app.triptiledetails.TripTileDetailsViewModel
import com.example.wherenow.ui.app.triptiledetails.filetile.FileViewModel
import com.example.wherenow.ui.app.triptiledetails.importantnotes.ImportantNotesViewModel
import com.example.wherenow.ui.app.triptiledetails.importantnotes.blanknote.BlankNoteViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::TripListViewModel)
    viewModelOf(::TripDataDetailsViewModel)
    viewModelOf(::TripTileDetailsViewModel)
    viewModelOf(::ImportantNotesViewModel)
    viewModelOf(::BlankNoteViewModel)
    viewModelOf(::StatedVisitedViewModel)
    viewModelOf(::FileViewModel)
    viewModelOf(::AppThemeViewModel)
}
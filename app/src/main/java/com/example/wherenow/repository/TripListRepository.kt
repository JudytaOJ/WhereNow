package com.example.wherenow.repository

import com.example.wherenow.repository.models.TripListItemData
import javax.inject.Inject

interface TripListRepository {
    fun saveListDataTile(data: MutableList<TripListItemData?>)
    fun getListDataTile(): MutableList<TripListItemData?>
    fun clear()
}

class TripListRepositoryImpl @Inject constructor() : TripListRepository {

    private var listDataTile: MutableList<TripListItemData?> = mutableListOf()

    override fun saveListDataTile(data: MutableList<TripListItemData?>) {
        listDataTile = data
    }

    override fun getListDataTile(): MutableList<TripListItemData?> = listDataTile

    override fun clear() {
        listDataTile = mutableListOf()
    }
}
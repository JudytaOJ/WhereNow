package com.example.wherenow.repository

import com.example.wherenow.repository.models.TripListItemData
import javax.inject.Inject

interface TripListRepository {
    fun saveDataTile(data: TripListItemData?)
    fun getDataTile(): TripListItemData?
    fun clear()
}

class TripListRepositoryImpl @Inject constructor() : TripListRepository {

    private var dataTile: TripListItemData? = null

    override fun saveDataTile(data: TripListItemData?) {
        dataTile = data
    }

    override fun getDataTile(): TripListItemData? = dataTile

    override fun clear() {
        dataTile = null
    }
}
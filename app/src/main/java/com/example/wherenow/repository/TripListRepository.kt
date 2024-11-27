package com.example.wherenow.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.wherenow.repository.models.TripListData
import com.example.wherenow.repository.models.TripListItemData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface TripListRepository {
    suspend fun saveListDataTile(data: MutableList<TripListItemData>)
    suspend fun getListDataTile(): MutableList<TripListItemData>
}

val Context.dataStore: DataStore<TripListData> by dataStore(fileName = "TRIP_LIST", serializer = TripListSerializer)

class TripListRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : TripListRepository {

    override suspend fun saveListDataTile(data: MutableList<TripListItemData>) {
        context.dataStore.updateData { it.copy(tripList = data) }
    }

    override suspend fun getListDataTile(): MutableList<TripListItemData> = context.dataStore.data.first().tripList
}
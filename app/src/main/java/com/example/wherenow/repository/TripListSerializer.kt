package com.example.wherenow.repository

import android.util.Log
import androidx.datastore.core.Serializer
import com.example.wherenow.repository.models.TripListData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object TripListSerializer : Serializer<TripListData> {
    override val defaultValue: TripListData = TripListData(mutableListOf())

    override suspend fun readFrom(input: InputStream): TripListData {
        return try {
            Json.decodeFromString(
                deserializer = TripListData.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            Log.d("error read from method", e.message.orEmpty())
            defaultValue
        }
    }

    override suspend fun writeTo(t: TripListData, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = TripListData.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}
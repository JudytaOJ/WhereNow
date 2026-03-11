package com.example.wherenow.ui.app.settingsmenu.flightStatistics.components

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import java.io.InputStream

class GeoJsonParser {
    fun parseGeoJson(inputStream: InputStream): List<JsonElement> {
        val geoJsonString = inputStream.bufferedReader().use { it.readText() }
        val json = Json.parseToJsonElement(geoJsonString).jsonObject
        return json["features"]?.jsonArray?.toList() ?: emptyList()
    }
}
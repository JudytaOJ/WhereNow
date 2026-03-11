package com.example.wherenow.repository.statistics

import android.content.Context
import com.example.wherenow.R
import com.example.wherenow.ui.app.settingsmenu.flightStatistics.components.GeoJsonParser
import kotlinx.serialization.json.JsonElement

class FlightStatisticsRepository(private val context: Context) {
    fun getFeatures(): List<JsonElement> {
        val inputStream = context.resources.openRawResource(R.raw.us_states_map)
        return GeoJsonParser().parseGeoJson(inputStream)
    }
}
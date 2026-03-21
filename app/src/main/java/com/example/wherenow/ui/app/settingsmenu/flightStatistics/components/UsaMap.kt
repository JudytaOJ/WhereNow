package com.example.wherenow.ui.app.settingsmenu.flightStatistics.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.wherenow.util.StringUtils
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun USAMap(
    features: List<JsonElement>?,
    visitedStates: List<String>,
    modifier: Modifier = Modifier
) {
    val colorVisitedState = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
    val colorLineState = MaterialTheme.colorScheme.onSurface

    // Function for transforming the coordinates of Alaska and Hawaii
    fun transformStateCoords(lon: Float, lat: Float, stateName: String): Pair<Float, Float> {
        var newLon = lon
        var newLat = lat

        when (stateName) {
            "Alaska" -> {
                val scale = 0.35f
                val originLon = -150f
                val originLat = 63f

                // scaling
                newLon = originLon + (lon - originLon) * scale
                newLat = originLat + (lat - originLat) * scale

                // shift relative to the continental map
                newLon += 35f
                newLat -= 37f
            }

            "Hawaii" -> {
                newLon += 58f
                newLat -= 2f
            }
        }

        return newLon to newLat
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (features.isNullOrEmpty()) return@Canvas

        // Collect all points with transformations
        val allPoints = mutableListOf<Pair<Float, Float>>()
        features.forEach { featureElement ->
            val feature = featureElement.jsonObject
            val stateName = feature["properties"]?.jsonObject?.get("name")?.jsonPrimitive?.content ?: StringUtils.EMPTY
            val geometry = feature["geometry"]?.jsonObject ?: return@forEach
            val type = geometry["type"]?.jsonPrimitive?.content
            val coordinates = geometry["coordinates"] ?: return@forEach

            fun collectCoords(coordArray: JsonArray) {
                coordArray.forEach { pointArray ->
                    val lon = pointArray.jsonArray[0].jsonPrimitive.double.toFloat()
                    val lat = pointArray.jsonArray[1].jsonPrimitive.double.toFloat()
                    val (tLon, tLat) = transformStateCoords(lon, lat, stateName)
                    allPoints.add(tLon to tLat)
                }
            }

            when (type) {
                "Polygon" -> coordinates.jsonArray.forEach { ring -> collectCoords(ring.jsonArray) }
                "MultiPolygon" -> coordinates.jsonArray.forEach { polygon ->
                    polygon.jsonArray.forEach { ring -> collectCoords(ring.jsonArray) }
                }
            }
        }

        // Bounding box of the entire map (including Alaska and Hawaii) 🗺️
        val xMin = allPoints.minOf { it.first }
        val xMax = allPoints.maxOf { it.first }
        val yMin = allPoints.minOf { it.second }
        val yMax = allPoints.maxOf { it.second }

        val mapWidth = xMax - xMin
        val mapHeight = yMax - yMin

        // Proportional scale
        val scale = minOf(size.width / mapWidth, size.height / mapHeight)
        val offsetX = (size.width - mapWidth * scale) / 2f
        val offsetY = (size.height - mapHeight * scale) / 2f

        // Drawing the states
        features.forEach { featureElement ->
            val feature = featureElement.jsonObject
            val stateName = feature["properties"]?.jsonObject?.get("name")?.jsonPrimitive?.content ?: StringUtils.EMPTY
            val geometry = feature["geometry"]?.jsonObject ?: return@forEach
            val type = geometry["type"]?.jsonPrimitive?.content
            val coordinates = geometry["coordinates"] ?: return@forEach

            fun buildPath(coordArray: JsonArray) {
                val path = Path()
                coordArray.forEachIndexed { index, pointArray ->
                    val lon = pointArray.jsonArray[0].jsonPrimitive.double.toFloat()
                    val lat = pointArray.jsonArray[1].jsonPrimitive.double.toFloat()
                    val (tLon, tLat) = transformStateCoords(lon, lat, stateName)

                    val x = (tLon - xMin) * scale + offsetX
                    val y = (yMax - tLat) * scale + offsetY

                    if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                path.close()

                if (visitedStates.contains(stateName)) {
                    drawPath(path, color = colorVisitedState, style = Fill)
                }

                drawPath(path, color = colorLineState, style = Stroke(width = 1f))
            }

            when (type) {
                "Polygon" -> coordinates.jsonArray.forEach { ring -> buildPath(ring.jsonArray) }
                "MultiPolygon" -> coordinates.jsonArray.forEach { polygon ->
                    polygon.jsonArray.forEach { ring -> buildPath(ring.jsonArray) }
                }
            }
        }
    }
}
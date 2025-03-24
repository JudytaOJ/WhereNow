package com.example.wherenow.data.usecases

import com.example.wherenow.repository.TripListRepository
import com.example.wherenow.repository.models.TripListItemData

class GetListDataTileUseCase internal constructor(
    private val tripListRepository: TripListRepository
) {
    suspend operator fun invoke(): List<TripListItemData> =
        tripListRepository.getListDataTile().map {
            TripListItemData(
                date = it.date,
                image = it.image,
                departureCity = it.departureCity,
                departureCountry = it.departureCountry,
                departureAirport = it.departureAirport,
                arrivalCity = it.arrivalCity,
                arrivalCountry = it.arrivalCountry,
                arrivalAirport = it.arrivalAirport,
                arrivalCodeAirport = it.arrivalCodeAirport,
                departureCodeAirport = it.departureCodeAirport,
                distance = it.distance,
                id = it.id
            )
        }.sortedBy { date -> date.date }
}
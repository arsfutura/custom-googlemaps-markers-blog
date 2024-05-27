package com.example.customgooglemapsmarkers

import com.google.android.gms.maps.model.LatLng

class DataProvider {
    companion object {
        fun getMapMarkers(): List<MapMarkerObject> {
            val marker1 = MapMarkerObject(
                sequenceId = 1,
                locationName = "Arena Centar",
                coordinates = LatLng(45.77, 15.93)
            )
            val marker2 = MapMarkerObject(
                sequenceId = 2,
                completed = true,
                category = Category.RESTOCK,
                locationName = "Avenue Mall",
                coordinates = LatLng(45.77, 15.97)
            )
            val marker3 = MapMarkerObject(
                sequenceId = 0,
                depot = true,
                locationName = "Ars Futura",
                coordinates = LatLng(45.79, 15.95)
            )
            val marker4 = MapMarkerObject(
                sequenceId = 3,
                delay = Delay.DELAY_AT_RISK,
                locationName = "Supernova Garden Mall",
                coordinates = LatLng(45.83, 16.04)
            )
            val marker5 = MapMarkerObject(
                sequenceId = 4,
                delay = Delay.DELAY_LATE,
                locationName = "City Center one West",
                coordinates = LatLng(45.79, 15.88)
            )
            val marker6 = MapMarkerObject(
                sequenceId = 5,
                completed = true,
                locationName = "City Center one East",
                coordinates = LatLng(45.80, 16.05)
            )
            val marker7 = MapMarkerObject(
                sequenceId = 6,
                completed = true,
                delay = Delay.DELAY_LATE,
                locationName = "Westgate Shopping City",
                coordinates = LatLng(45.87, 15.82)
            )
            return listOf(marker1, marker2, marker3, marker4, marker5, marker6, marker7)
        }
    }
}
package com.example.customgooglemapsmarkers

import com.google.android.gms.maps.model.LatLng

data class MapMarkerObject(
    val sequenceId: Int,
    val completed: Boolean = false,
    val depot: Boolean = false,
    private val delay: Delay = Delay.ON_TIME,
    val category: Category = Category.MAP_MARKER,
    val locationName: String = "",
    val coordinates: LatLng
) {

    val outerRing: Int
        get() = if (shouldShowIcon()) color else R.color.white

    val innerRing: Int
        get() = if (shouldShowIcon()) R.color.white else color

    val drawable: Int
        get() = if (depot) R.drawable.ic_depot else when (category) {
            Category.RESTOCK -> R.drawable.ic_refresh
            else -> R.drawable.ic_check
        }

    fun shouldShowIcon() = completed || depot || category == Category.RESTOCK

    val color: Int
        get() = if (depot) R.color.depot else when (delay) {
            Delay.DELAY_AT_RISK -> R.color.at_risk
            Delay.DELAY_LATE -> R.color.delayed
            else -> R.color.on_time
        }
}

enum class Category {
    MAP_MARKER, PARKING, RESTOCK
}

enum class Delay {
    ON_TIME, DELAY_AT_RISK, DELAY_LATE
}

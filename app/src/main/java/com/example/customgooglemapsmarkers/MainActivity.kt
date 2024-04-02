package com.example.customgooglemapsmarkers

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.customgooglemapsmarkers.databinding.ActivityMainBinding
import com.example.customgooglemapsmarkers.databinding.MapMarkerBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

const val MAP_MARKER_SIZE = 45
const val MAP_MARKER_STROKE = 3

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                val mapFragment: SupportMapFragment? =
                    supportFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
                googleMap = mapFragment?.awaitMap()

                val builder = LatLngBounds.Builder()

                for (mapMarker in getMapMarkers()) {
                    builder.include(mapMarker.coordinates)
                    addAssignmentMarker(mapMarker)
                }
                val bounds = builder.build()
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 80)

                googleMap?.moveCamera(cameraUpdate)
            }
        }
    }

    private fun addAssignmentMarker(mapMarkerObject: MapMarkerObject) {
        googleMap?.addMarker {
            position(mapMarkerObject.coordinates)
            icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mapMarkerObject)))
            title(mapMarkerObject.locationName)
        }
    }

    private fun getMapMarkers(): List<MapMarkerObject> {
        val marker1 = MapMarkerObject(
            1,
            locationName = "Arena Centar", coordinates = LatLng(45.77, 15.93)
        )
        val marker2 = MapMarkerObject(
            2, completed = true,
            category = Category.RESTOCK,
            locationName = "Avenue Mall", coordinates = LatLng(45.77, 15.97)
        )
        val marker3 = MapMarkerObject(
            0, depot = true,
            locationName = "Ars Futura", coordinates = LatLng(45.79, 15.95)
        )
        val marker4 = MapMarkerObject(
            3,
            delay = Delay.DELAY_AT_RISK,
            locationName = "Supernova Garden Mall", coordinates = LatLng(45.83, 16.04)
        )
        val marker5 = MapMarkerObject(
            4,
            delay = Delay.DELAY_LATE,
            locationName = "City Center one West", coordinates = LatLng(45.79, 15.88)
        )
        val marker6 = MapMarkerObject(
            5, completed = true,
            locationName = "City Center one East", coordinates = LatLng(45.80, 16.05)
        )
        val marker7 = MapMarkerObject(
            6, completed = true,
            delay = Delay.DELAY_LATE,
            locationName = "Westgate Shopping City", coordinates = LatLng(45.87, 15.82)
        )
        return listOf(marker1, marker2, marker3, marker4, marker5, marker6, marker7)
    }

    private fun getMarkerBitmapFromView(marker: MapMarkerObject): Bitmap {
        val markerBinding: MapMarkerBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.map_marker,
            binding.mapFragment,
            false
        )
        markerBinding.mapMarker = marker
        markerBinding.lifecycleOwner = this
        markerBinding.customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        markerBinding.customMarkerView.layout(0, 0, dpToPx(MAP_MARKER_SIZE), dpToPx(MAP_MARKER_SIZE))
        markerBinding.linearLayout.layout(
            dpToPx(MAP_MARKER_STROKE), dpToPx(MAP_MARKER_STROKE),
            dpToPx(MAP_MARKER_SIZE - MAP_MARKER_STROKE),
            dpToPx(MAP_MARKER_SIZE - MAP_MARKER_STROKE)
        )

        val returnedBitmap = Bitmap.createBitmap(
            dpToPx(MAP_MARKER_SIZE), dpToPx(MAP_MARKER_SIZE),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable: Drawable = markerBinding.customMarkerView.background
        drawable.draw(canvas)
        markerBinding.customMarkerView.draw(canvas)
        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteArrayOutputStream())
        return returnedBitmap
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

}
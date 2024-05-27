package com.example.customgooglemapsmarkers

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
                val mapMarkers = DataProvider.getMapMarkers()

                for (mapMarker in mapMarkers) {
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

    private fun getMarkerBitmapFromView(marker: MapMarkerObject): Bitmap {
        val markerBinding: MapMarkerBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.map_marker,
            binding.mapFragment,
            false
        )
        markerBinding.mapMarker = marker
        markerBinding.lifecycleOwner = this
        markerBinding.customMarkerView.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )
        markerBinding.customMarkerView.layout(
            0,
            0,
            dpToPx(MAP_MARKER_SIZE),
            dpToPx(MAP_MARKER_SIZE)
        )
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
}
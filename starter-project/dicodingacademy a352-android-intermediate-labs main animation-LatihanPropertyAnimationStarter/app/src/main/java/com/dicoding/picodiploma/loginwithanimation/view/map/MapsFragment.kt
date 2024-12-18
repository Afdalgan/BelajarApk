package com.dicoding.picodiploma.loginwithanimation.view.map

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val viewModel: MapsViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        setMapStyle()

        viewModel.storiesWithLocation.observe(viewLifecycleOwner) { stories ->
            if (stories.isNotEmpty()) {
                val boundsBuilder = LatLngBounds.Builder()

                stories.forEach { story ->
                    if (story.lat != null && story.lon != null) {
                        val latLng = LatLng(story.lat, story.lon)

                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(story.name)
                                .snippet(story.description)
                        )

                        boundsBuilder.include(latLng)
                    }
                }

                try {
                    val bounds = boundsBuilder.build()
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                } catch (e: IllegalStateException) {
                    Log.e(TAG, "Error moving camera: ", e)
                }
            }
        }

        viewModel.getStoriesWithLocation()
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style)
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }
}

package com.example.inzynierka

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.inzynierka.databinding.FragmentMapBinding
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.common.location.compat.permissions.PermissionsListener
import com.mapbox.common.location.compat.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.maps.plugin.viewport.viewport


class MapFragment : Fragment(), LocationEngineCallback<LocationEngineResult> {

    companion object {
        private const val TAG = "MapFragment"
        private const val REQUEST_LOCATION_UPDATE_INTERVAL = 1000L
    }

    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView
    private val locationPermissionGranted: Boolean
        get() = PermissionsManager.areLocationPermissionsGranted(requireContext())
    private val permissionsManager = PermissionsManager(object : PermissionsListener {
        override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {}

        override fun onPermissionResult(granted: Boolean) {
            if (granted) {
                // Разрешения получены, можно выполнять логику, требующую разрешений
                activateLocationComponent()
            } else {
                // Пользователь отказал в предоставлении разрешений, можно предпринять дальнейшие действия
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMap()
        verifyLocationPermission()
        setLocationButtonClickListener()
    }

    private fun setUpMap() {
        mapView = binding.mapView
        mapView.scalebar.enabled = false

        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder().center(Point.fromLngLat(21.010872, 52.220370)).pitch(0.0).zoom(10.0).bearing(0.0).build()
        )
    }

    private fun verifyLocationPermission() {
        if (locationPermissionGranted) {
            // Разрешения на местоположение уже предоставлены
            // Можно выполнить логику, требующую разрешений
            activateLocationComponent()
        } else {
            permissionsManager.requestLocationPermissions(requireActivity())
        }
    }

    @SuppressLint("MissingPermission")
    private fun activateLocationComponent() {
        // логика активации компонента местоположения
        with(mapView) {
//                location.locationPuck = createDefault2DPuck(withBearing = true)
            location.enabled = true
//                location.puckBearing = PuckBearing.COURSE
            viewport.transitionTo(
                targetState = viewport.makeFollowPuckViewportState(), transition = viewport.makeImmediateViewportTransition()
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun setLocationButtonClickListener() {
        binding.location.setOnClickListener {
            if (!locationPermissionGranted) return@setOnClickListener
            val locationEngine = LocationEngineProvider
                .getBestLocationEngine(requireContext())
            locationEngine.requestLocationUpdates(
                LocationEngineRequest.Builder(REQUEST_LOCATION_UPDATE_INTERVAL)
                    .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                    .build(),
                this@MapFragment,
                Looper.getMainLooper()
            )
        }
    }

    override fun onSuccess(result: LocationEngineResult?) {
        val location = result?.lastLocation
        if (location == null) {
            Log.w(TAG, "lastLocation is null")
            return
        }
        val latitude = location.latitude
        val longitude = location.longitude
        showMessage("latitude: $latitude; longitude: $longitude")
    }

    private fun showMessage(message: String) {
        Log.d(TAG, message)
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(exception: Exception) {
        showMessage(exception.localizedMessage ?: "Nie udało się pobrać lokalizacji urządzenia.")
    }
}
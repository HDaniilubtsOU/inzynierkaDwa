package com.example.inzynierka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.inzynierka.databinding.FragmentMapBinding
import com.mapbox.common.location.compat.permissions.PermissionsListener
import com.mapbox.common.location.compat.permissions.PermissionsManager
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.maps.plugin.viewport.viewport


class MapFragment : Fragment(){
    private lateinit var binding: FragmentMapBinding
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(R.layout.fragment_map, container, false)
//        return view
        binding = FragmentMapBinding.inflate(inflater, container, false)

        mapView = binding.mapView
        mapView.scalebar.enabled = false

        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(21.010872,52.220370))
                .pitch(0.0)
                .zoom(10.0)
                .bearing(0.0)
                .build()
        )


        fun activateLocationComponent() {
            // логика активации компонента местоположения
            with(mapView) {
//                location.locationPuck = createDefault2DPuck(withBearing = true)
                location.enabled = true
//                location.puckBearing = PuckBearing.COURSE
                viewport.transitionTo(
                    targetState = viewport.makeFollowPuckViewportState(),
                    transition = viewport.makeImmediateViewportTransition()
                )
            }
        }
        val permissionsManager = PermissionsManager(object : PermissionsListener {
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

        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            // Разрешения на местоположение уже предоставлены
            // Можно выполнить логику, требующую разрешений
            activateLocationComponent()
        } else {
            permissionsManager.requestLocationPermissions(requireActivity())
        }

        return binding.root
    }
}
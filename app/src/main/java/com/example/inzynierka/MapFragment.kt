package com.example.inzynierka

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback{
//    private lateinit var gMap: GoogleMap
//    private lateinit var map: FrameLayout

//    private lateinit var mMap: GoogleMap
//    private lateinit var binding: MapFragment
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_map, container, false)

        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return view

//        map = findViewById(R.id.map)
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapsAsync(this)


    }


    override fun onMapReady(p0: GoogleMap) {
        p0.let {
            googleMap = it
            // Настройте карту здесь
            googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(0.0, 0.0))
                    .title("Marker")
            )
        }
//        mMap = p0
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(
//            MarkerOptions()
//                .position(sydney)
//                .title("Marker in Sydney")
//        )
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
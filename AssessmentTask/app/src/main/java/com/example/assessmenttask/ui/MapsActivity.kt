package com.example.assessmenttask.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.assessmenttask.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.assessmenttask.databinding.ActivityMapsBinding
import com.example.assessmenttask.models.Places
import com.example.assessmenttask.service.ApiClient
import com.example.assessmenttask.service.NearbyServiceApi
import com.google.android.gms.dynamic.IObjectWrapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var location: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var myService: NearbyServiceApi
    private var lat by Delegates.notNull<Double>()
    private var lon by Delegates.notNull<Double>()
    internal lateinit var currentPlace: Places

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lat = 00.0
        lon = 00.0
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


    }

    private fun getNearbyRestaurant(latitude: Double, longitude: Double) {
        myService = ApiClient.getApiService()

        val url = getUrl(latitude, longitude, "restaurant")
        myService.getNearbyRestaurant(url)
            .enqueue(object : Callback<Places> {
                override fun onResponse(call: Call<Places>, response: Response<Places>) {
                    currentPlace = response.body()!!
                    if (response.isSuccessful) {
                        for (i in 0 until (response.body()!!.results?.size!!)) {
                            val markerOptions = MarkerOptions()
                            val googlePlace = response.body()!!.results!![i]
                            val lat = googlePlace.geometry!!.location!!.lat!!
                            val lon = googlePlace.geometry!!.location!!.lng!!
                            val placeName = googlePlace.name
                            val latLng = LatLng(lat, lon)
                            markerOptions.position(latLng)
                            markerOptions.title(placeName)
                            markerOptions.icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_GREEN
                                )
                            )

                            mMap.addMarker(markerOptions)
                        }

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lon)))
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(12f))
                    }
                }

                override fun onFailure(call: Call<Places>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "Some thing rong", Toast.LENGTH_LONG).show()
                }


            })
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        settingMap()


    }

    private fun settingMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) {
            if (it != null) {
                location = it
                val currentLocation = LatLng(it.latitude, it.longitude)
                lat = it.latitude
                lon = it.longitude
                mMap.addMarker(MarkerOptions().position(currentLocation).title("Current Location"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f))
                Toast.makeText(this, "show nearby Restaurant", Toast.LENGTH_LONG).show()
                getNearbyRestaurant(lat, lon)

            }
        }

    }

    private fun getUrl(latitude: Double, longitude: Double, s: String): String {

        val googlePlaceUrl =
            StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longitude")
        googlePlaceUrl.append("&radius=100000")
        googlePlaceUrl.append("&type=restaurant")
        googlePlaceUrl.append("&key=AIzaSyAiAir1uMz3NwJDd9vjIhqeEuTUgw2S7VM")

        return googlePlaceUrl.toString()
    }

    override fun onMarkerClick(p0: Marker) = false
}
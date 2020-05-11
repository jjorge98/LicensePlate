package br.com.licenseplate.views.activities.client

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.TextUtils.indexOf
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.AdmViewModel
import br.com.licenseplate.views.adapter.StoreMapAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_store_maps.*
import java.io.IOException

class StoreMapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    //    private lateinit var nome: String
//    private lateinit var cpf: String
//    private lateinit var cel: String
//    private lateinit var carroID: String
//    private lateinit var uf: String
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private val viewModelA: AdmViewModel by lazy {
        ViewModelProvider(this).get(AdmViewModel::class.java)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_maps)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//        val intent = this.intent
//        nome = intent.getStringExtra("nome")
//        cpf = intent.getStringExtra("cpf")
//        cel = intent.getStringExtra("cel")
//        carroID = intent.getStringExtra("carroID")
//        uf = intent.getStringExtra("uf")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun storesRecyclerView() {
        viewModelA.storeList.observe(this, Observer { store ->
            storesMapRecyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = StoreMapAdapter(store, applicationContext, map)
            storesMapRecyclerView.adapter = adapter

            store.forEach { s ->
                val latitude = s.localizacao?.substring(0, indexOf(s.localizacao, ','))?.toDouble()
                val longitude =
                    s.localizacao?.substring(indexOf(s.localizacao, ", ") + 1)?.toDouble()

                if (latitude != null && longitude != null) {
                    val currentLatLng = LatLng(latitude, longitude)
                    placeMarkerOnMap(currentLatLng)
                }
            }
        })

        viewModelA.storeListAdm()
    }

    //TODO: Arrumar o nome que fica quando clica num icone
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        storesRecyclerView()
        // Add a marker and move the camera
//        val myPlace = LatLng(-15.773028, -47.778600)
//        map.addMarker(MarkerOptions().position(myPlace).title("Marker in my place"))
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 15.0f))

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f))
            }
        }
    }

    //Função que coloca um marcador no mapa.
    private fun placeMarkerOnMap(location: LatLng) {
        val titleStr = getAddress(location)
        val markerOptions = MarkerOptions().position(location).title(titleStr)

        map.addMarker(markerOptions)
    }

    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        var addressText = ""

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            if (null != addresses && addresses.isNotEmpty()) {
                addressText = addresses[0].getAddressLine(0)
//                for (i in 0 until address.maxAddressLineIndex) {
//                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(
//                        i
//                    )
//                }
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.showInfoWindow()
        return true
    }
}

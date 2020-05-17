package br.com.licenseplate.views.activities.client

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.TextUtils.indexOf
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.licenseplate.R
import br.com.licenseplate.viewmodel.ClientViewModel
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

class StoreMapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    //início variaveis vindas de activities anteriores
    var nome: String? = null
    var cpf: String? = null
    var cel: String? = null
    var carroID: String? = null
    var uf: String? = null
    //fim variáveis vindas de activities anteriores

    var id = 0
    val root = "autorizacaoCliente"
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var lastSelectedMark: Marker? = null

    private val viewModelC: ClientViewModel by lazy {
        ViewModelProvider(this).get(ClientViewModel::class.java)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_maps)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val intent = this.intent
        nome = intent.getStringExtra("nome")
        cpf = intent.getStringExtra("cpf")
        cel = intent.getStringExtra("cel")
        carroID = intent.getStringExtra("carroID")
        uf = intent.getStringExtra("uf")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun storesRecyclerView(location: LatLng) {
        viewModelC.storeList.observe(this, Observer { store ->
            storesMapRecyclerView.layoutManager = LinearLayoutManager(this)
            val adapter = StoreMapAdapter(store, this, map, this, viewModelC)
            storesMapRecyclerView.adapter = adapter

            store.forEach { s ->
                val latitude = s.localizacao?.substring(0, indexOf(s.localizacao, ','))?.toDouble()
                val longitude =
                    s.localizacao?.substring(indexOf(s.localizacao, ", ") + 1)?.toDouble()

                if (latitude != null && longitude != null) {
                    val currentLatLng = LatLng(latitude, longitude)
                    placeMarkerOnMap(currentLatLng, s.id)
                }
            }
        })

        viewModelC.storeList(location)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a marker and move the camera
//        val myPlace = LatLng(0.0, 0.0)
//        map.addMarker(MarkerOptions().position(myPlace).title("Marker in my place"))
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 15.0f))

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        setUpMap(googleMap)
    }

    private fun setUpMap(googleMap: GoogleMap) {
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
                storesRecyclerView(currentLatLng)

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f))
            }
        }
    }

    //Função que coloca um marcador no mapa.
    private fun placeMarkerOnMap(location: LatLng, id: Int?) {
        viewModelC.getAddress(location) { titleStr ->
            val markerOptions = MarkerOptions().position(location).title(titleStr)

            map.addMarker(markerOptions).tag = id
        }

    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        viewModelC.verifyID(root) { result ->
            id = result
        }
        if (lastSelectedMark != null && lastSelectedMark == marker) {
            viewModelC.saveAuthorization(carroID, uf, nome, cel, cpf, marker?.tag as Int?, id)
            val intent = Intent(this, FinishedRequest::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        } else {
            lastSelectedMark = marker
            marker?.showInfoWindow()
        }

        return true
    }
}

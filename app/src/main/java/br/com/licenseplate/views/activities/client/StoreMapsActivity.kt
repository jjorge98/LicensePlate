package br.com.licenseplate.views.activities.client

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils.indexOf
import android.widget.Toast
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
//TODO: Fazer filtro de localização nas lojas pra mostrar no mapa
class StoreMapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    //início variaveis vindas de activities anteriores
    private var nome: String? = null
    private var cpf: String? = null
    private var cel: String? = null
    private var placa: String? = null
    private var uf: String? = null
    private var numAutorizacao: String? = null
    private var materiais: String? = null
    private var categoria: String? = null
    //fim variáveis vindas de activities anteriores

    var id = 0
    private val root = "autorizacaoCliente"
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    var lastSelectedMark: Marker? = null

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
        placa = intent.getStringExtra("placa")
        uf = intent.getStringExtra("uf")
        numAutorizacao = intent.getStringExtra("numAutorizacao")
        materiais = intent.getStringExtra("materiais")
        categoria = intent.getStringExtra("categoria")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        buttonMap.setOnClickListener { next() }
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
        lastSelectedMark = marker
        marker?.showInfoWindow()

        return true
    }

    private fun next() {
        viewModelC.verifyID(root) { result ->
            id = result
        }

        Handler().postDelayed({
            viewModelC.saveAuthorization(
                placa,
                nome,
                cel,
                cpf,
                lastSelectedMark,
                id,
                numAutorizacao,
                materiais,
                categoria
            ) { response ->
                if (response[0] == "ERROR") {
                    Toast.makeText(this, response[1], Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, FinishedRequest::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                    finish()
                }
            }
        }, 2000)
    }
}

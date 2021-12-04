package com.hilmihanif.infomasigempa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.hilmihanif.infomasigempa.adapter.NetworkConfig
import com.hilmihanif.infomasigempa.data.Gempa
import com.hilmihanif.infomasigempa.data.InfoGempa
import com.hilmihanif.infomasigempa.data.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(),OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    companion object{
        const val EXTRA_LOCATION_PERMISION: String = "EXTRA_LOCATION_PERMISION"
        const val EXTRA_LIST_GEMPA: String = "EXTRA_LIST_GEMPA"
        const val LOCATION_PERMISSION_CODE = 101
    }

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var userLastLocation :LatLng
    private lateinit var listAlamat: MutableList<Address>
    private lateinit var geocoder:Geocoder
    private lateinit var mapFragment:SupportMapFragment

    private lateinit var tvEstimasiLokasi:TextView


    val listGempa = mutableListOf<Gempa>()
    val markerList  = mutableListOf<Marker>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val btn_dataLengkap = findViewById<Button>(R.id.btn_dataLengkap)
        tvEstimasiLokasi = findViewById(R.id.tv_estimasiLokasi)



        val resultGempa  = intent.getParcelableExtra<InfoGempa>(GempaListActivity.EXTRA_LIST_GEMPA)
        resultGempa?.let {
            listGempa.addAll(it.gempa)

        }



        Log.d("check list gempa", listGempa.toString())

        btn_dataLengkap.setOnClickListener() {
            Log.d("Button Click",resultGempa.toString())
            val intent = Intent(this@MainActivity, GempaListActivity::class.java)
            intent.putExtra(GempaListActivity.EXTRA_LIST_GEMPA,resultGempa)
            startActivity(intent)

        }




        if (isLocationPermissionGranted()) {
            mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

        } else {
            requestLocationPermission()
            finish()
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }







    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        geocoder = Geocoder(this, Locale.getDefault())
        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        mMap.setMinZoomPreference(3f)
        mMap.setMaxZoomPreference(7f)


        if(ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true)

        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let{
                    userLastLocation = LatLng(it.latitude,it.longitude)
                    mMap.addMarker(MarkerOptions().position(userLastLocation).title("Lokasi Anda"))

                    mMap.moveCamera(CameraUpdateFactory
                        .newLatLng(userLastLocation))

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,6f))

                    listAlamat = geocoder.getFromLocation(userLastLocation.latitude,userLastLocation.longitude,1)
                    if(listAlamat.isNotEmpty()){
                        val namaKota = listAlamat.get(0).subAdminArea
                        val namaProvinsi = listAlamat.get(0).adminArea

                        tvEstimasiLokasi.text = "Estimasi Lokasi Anda Berada di $namaKota,$namaProvinsi"
                    }



                }
            }.addOnCanceledListener {
                requestLocationPermission()
                userLastLocation = LatLng(0.78,113.92)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,10f))
            }



        for(i in listGempa ){
            val temp:Marker? = mMap.addMarker(MarkerOptions()
                .position(i.getLatLong())
                .title(i.wilayah))
            Log.d("check marker",i.toString())
            if (temp != null) {
                markerList.add(temp)
            }

        }
        Log.d("check filter",listGempa.toString())

        mMap.setOnMarkerClickListener(this)

    }





    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_CODE
        )
    }


    override fun onMarkerClick(marker: Marker): Boolean {
        Toast.makeText(this,"Test ${marker.position}",Toast.LENGTH_SHORT).show()

        return false
    }


}
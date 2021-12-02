package com.hilmihanif.infomasigempa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
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
import com.hilmihanif.infomasigempa.data.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(),OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    companion object{

        const val LOCATION_PERMISSION_CODE = 101
    }

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var userLastLocation :LatLng

    lateinit var mapFragment:SupportMapFragment


    var listGempa = mutableListOf<Gempa>()
    var markerList  = mutableListOf<Marker>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btn_dataLengkap = findViewById<Button>(R.id.btn_dataLengkap)


        NetworkConfig().getService().getAllData().enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d("Button Click",call.toString())
                val data = response.body()
                data?.let{

                    Log.d("check data",it.toString())
                    listGempa.addAll(it.infoGempa.gempa)

                    btn_dataLengkap.setOnClickListener() {
                        Log.d("Button Click",data.toString())
                        val intent = Intent(this@MainActivity, GempaListActivity::class.java)
                        intent.putExtra(GempaListActivity.EXTRA_LIST_GEMPA,data.infoGempa)
                        startActivity(intent)

                    }
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.e("failure detected",t.toString())
            }
        })

        if (isLocationPermissionGranted()) {
            mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)


        } else {
            requestLocationPermission()



//            finish()
//            overridePendingTransition(0, 0);
//            startActivity(getIntent());
//            overridePendingTransition(0, 0);

        }







    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        mMap.setMinZoomPreference(5f)
        mMap.setMaxZoomPreference(5f)

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


                }
            }.addOnCanceledListener {
                requestLocationPermission()
                userLastLocation = LatLng(0.78,113.92)
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
        val i = 0
        for(i in markerList){
            if (marker.position == i.position){
                Toast.makeText(this,"Test ${marker.position.toString()}",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"lokasi anda  ${marker.position.toString()}",Toast.LENGTH_SHORT).show()
            }

        }
        return false
    }


}
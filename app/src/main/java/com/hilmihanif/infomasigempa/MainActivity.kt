package com.hilmihanif.infomasigempa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Transformations.map
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hilmihanif.infomasigempa.adapter.NetworkConfig
import com.hilmihanif.infomasigempa.data.Gempa
import com.hilmihanif.infomasigempa.data.Result
import com.hilmihanif.infomasigempa.data.InfoGempa
import com.hilmihanif.infomasigempa.data.KoordinatGempa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(),OnMapReadyCallback{


    companion object{

        const val LOCATION_PERMISSION_CODE = 101
    }

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var userLastLocation :LatLng

    lateinit var list: List<Gempa>
    lateinit var filteredData: List<KoordinatGempa>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btn_dataLengkap = findViewById<Button>(R.id.btn_dataLengkap)

        val onetimecheck = false;
        if (isLocationPermissionGranted()) {
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)


        } else {
            requestLocationPermission()
            finish()
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

        }




        NetworkConfig().getService().getAllData().enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val data = response.body()
                data?.let{

                    Log.d("check data",data.toString())
                    filteredData = filter_data(data.infoGempa.gempa)
                    btn_dataLengkap.setOnClickListener() {


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
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let{
                        userLastLocation = LatLng(it.latitude,it.longitude)
                        mMap.addMarker(MarkerOptions().position(userLastLocation).title("Lokasi Anda"))
                    }

                    filteredData?.let{
                        for(i in it ){
                               mMap.addMarker(MarkerOptions()
                                   .position(i.getLatLong())
                                   .title(i.wilayah))
                        }
                    }
                    mMap.moveCamera(CameraUpdateFactory
                        .newLatLng(userLastLocation))
                }
        }


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



    fun filter_data(listData: List<Gempa>):List<KoordinatGempa>{
        val listKoordinat = mutableListOf<KoordinatGempa>()
        for(data in listData){
            listKoordinat.add(KoordinatGempa(
                data.wilayah,
                data.lintang,
                data.bujur,
                ))
        }
        return listKoordinat.toList()
    }



}
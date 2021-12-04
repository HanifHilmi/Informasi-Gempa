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
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.dynamic.IObjectWrapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.hilmihanif.infomasigempa.adapter.NetworkConfig
import com.hilmihanif.infomasigempa.data.Gempa
import com.hilmihanif.infomasigempa.data.InfoGempa
import com.hilmihanif.infomasigempa.data.Result
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(),OnMapReadyCallback,GoogleMap.OnMarkerClickListener,GoogleMap.OnMapClickListener{

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
    private lateinit var cvMarkerClick: CardView
    private lateinit var tvWilayahMain :TextView
    private lateinit var tvDeskripsiMain :TextView
    private lateinit var tvMagnitudeMain :TextView
    private lateinit var tvPotensiGempa :TextView

    val listGempa = mutableListOf<Gempa>()
    val markerList  = mutableListOf<Marker>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val btn_dataLengkap = findViewById<Button>(R.id.btn_dataLengkap)
        tvEstimasiLokasi = findViewById(R.id.tv_estimasiLokasi)
        cvMarkerClick = findViewById(R.id.cv_markerclicked)
        tvWilayahMain = findViewById<TextView>(R.id.tv_wilayahMain)
        tvDeskripsiMain = findViewById<TextView>(R.id.tv_deskripsiMain)
        tvMagnitudeMain = findViewById<TextView>(R.id.tv_magnitudeMain)
        tvPotensiGempa = findViewById<TextView>(R.id.tv_potensigempaMain)



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
        mMap.setMaxZoomPreference(6f)


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


                    mMap.addMarker(MarkerOptions()
                        .position(userLastLocation)
                        .title("Lokasi Anda")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))!!
                        .showInfoWindow()

                    mMap.moveCamera(CameraUpdateFactory
                        .newLatLng(userLastLocation))

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,5f))

                    listAlamat = geocoder.getFromLocation(userLastLocation.latitude,userLastLocation.longitude,1)
                    if(listAlamat.isNotEmpty()){
                        val namaKota = listAlamat.get(0).subAdminArea
                        val namaProvinsi = listAlamat.get(0).adminArea

                        tvEstimasiLokasi.text = "Estimasi Lokasi Anda Saat ini Berada di $namaKota,$namaProvinsi"
                    }


                }
            }.addOnCanceledListener {
                requestLocationPermission()
                userLastLocation = LatLng(0.78,113.92)
                tvEstimasiLokasi.text = "Estimasi Lokasi Anda Saat ini Berada di indonesia"
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,3f))
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
        mMap.setOnMapClickListener(this)
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
        //Toast.makeText(this,"Test ${marker.position}",Toast.LENGTH_SHORT).show()

        for(item in listGempa){
            if(marker.title == item.wilayah ){
                cvMarkerClick.visibility = View.VISIBLE
                tvWilayahMain.text = item.wilayah
                tvDeskripsiMain.text = "Terjadi pada ${item.jam} ${item.tanggal} "
                tvMagnitudeMain.text = item.magnitude
                tvPotensiGempa.text = item.potensi

                if(item.potensi.subSequence(0,5).equals("Tidak")){
                    tvPotensiGempa.setTextColor(ContextCompat.getColor(this,R.color.green))
                }else{
                    tvPotensiGempa.setTextColor(ContextCompat.getColor(this,R.color.red))
                }
                cvMarkerClick.setOnClickListener(){
                    val intent = Intent(this,DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_GEMPA,item)
                    startActivity(intent)
                }

                break
            }else{
                cvMarkerClick.visibility = View.GONE
            }
        }


        return false
    }

    override fun onMapClick(p0: LatLng) {
        //Toast.makeText(this,"Test ${LatLng(p0.latitude,p0.latitude)}",Toast.LENGTH_SHORT).show()
        cvMarkerClick.visibility = View.GONE
    }


}
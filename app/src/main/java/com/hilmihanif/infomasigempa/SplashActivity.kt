package com.hilmihanif.infomasigempa

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.SupportMapFragment
import com.hilmihanif.infomasigempa.adapter.NetworkConfig
import com.hilmihanif.infomasigempa.data.Gempa
import com.hilmihanif.infomasigempa.data.Result
import com.hilmihanif.infomasigempa.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val TIMEOUT = 1000L
    private lateinit var mainActivtiyintent : Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)



        val listGempa = mutableListOf<Gempa>()

        mainActivtiyintent = Intent(this, MainActivity::class.java)



        NetworkConfig().getService().getAllData().enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d("Button Click", call.toString())
                val data = response.body()
                data?.let {
                    Log.d("Button Click", data.toString())


                    Log.d("check data", it.toString())
                    listGempa.addAll(it.infoGempa.gempa)
                    mainActivtiyintent.putExtra(MainActivity.EXTRA_LIST_GEMPA, data.infoGempa)


                }
            }


            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.e("failure detected", t.toString())
            }
        })


//        if (!isLocationPermissionGranted()) {
//            requestLocationPermission()
//            mainActivtiyintent.putExtra(MainActivity.EXTRA_LOCATION_PERMISION, false)
//
//        } else {
//            mainActivtiyintent.putExtra(MainActivity.EXTRA_LOCATION_PERMISION, true)
//        }





        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                mainActivtiyintent.putExtra(MainActivity.EXTRA_LOCATION_PERMISION, true)
                Handler(mainLooper).postDelayed({
                    startActivity(mainActivtiyintent)
                }, TIMEOUT)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {

            }
            else -> {
                // You can directly ask for the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    MainActivity.LOCATION_PERMISSION_CODE
                )
                mainActivtiyintent.putExtra(MainActivity.EXTRA_LOCATION_PERMISION, false)
            }
        }

        Handler(mainLooper).postDelayed({
            startActivity(mainActivtiyintent)
        }, TIMEOUT)


    }

    override fun onStart() {
        super.onStart()

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
            MainActivity.LOCATION_PERMISSION_CODE
        )
    }
}
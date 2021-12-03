package com.hilmihanif.infomasigempa

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.SupportMapFragment
import com.hilmihanif.infomasigempa.adapter.NetworkConfig
import com.hilmihanif.infomasigempa.data.Gempa
import com.hilmihanif.infomasigempa.data.Result
import com.hilmihanif.infomasigempa.MainActivity
import okhttp3.internal.http2.Http2Reader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val TIMEOUT = 1000L
    private lateinit var mainActivtiyintent : Intent
    private lateinit var pbSplash:ProgressBar
    private var i:Int = 0
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val pbSplash = findViewById<ProgressBar>(R.id.pb_splash)




        val listGempa = mutableListOf<Gempa>()

        mainActivtiyintent = Intent(this, MainActivity::class.java)


        NetworkConfig().getService().getAllData().enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                Log.d("check response", call.toString())
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

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                mainActivtiyintent.putExtra(MainActivity.EXTRA_LOCATION_PERMISION, true)
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

//        Handler(mainLooper).postDelayed({
//            startActivity(mainActivtiyintent)
//        }, TIMEOUT)

        i = pbSplash.progress
        Thread {
            // this loop will run until the value of i becomes 99
            while (i < 100) {
                i += 10
                // Update the progress bar and display the current value
                handler.post {
                    pbSplash.progress = i

                    if(pbSplash.progress==90 && listGempa.isNotEmpty()){
                        startActivity(mainActivtiyintent)
                        finish()
                    }else if(i == 90){
                        i=80
                    }

                }
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }







}
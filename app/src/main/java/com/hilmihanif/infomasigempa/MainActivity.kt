package com.hilmihanif.infomasigempa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.hilmihanif.infomasigempa.adapter.NetworkConfig
import com.hilmihanif.infomasigempa.data.Gempa
import com.hilmihanif.infomasigempa.data.Result
import com.hilmihanif.infomasigempa.data.InfoGempa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btn_dataLengkap = findViewById<Button>(R.id.btn_dataLengkap)





        NetworkConfig().getService().getAllData().enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val data = response.body()
                data?.let{

                    Log.d("check data",data.toString())
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
}
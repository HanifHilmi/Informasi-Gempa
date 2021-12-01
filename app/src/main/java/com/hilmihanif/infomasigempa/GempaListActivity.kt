package com.hilmihanif.infomasigempa

import com.hilmihanif.infomasigempa.adapter.NetworkConfig
import com.hilmihanif.infomasigempa.data.Result
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hilmihanif.infomasigempa.adapter.GempaAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GempaListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gempa_list)

        val rvGempa = findViewById<RecyclerView>(R.id.rv_listGempa)

        rvGempa.layoutManager = LinearLayoutManager(this)
        rvGempa.setHasFixedSize(true)
        val adapter = GempaAdapter(){
            Toast.makeText(this,"${it.wilayah}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_GEMPA, it)
            startActivity(intent)

        }
        rvGempa.adapter = adapter

        NetworkConfig().getService().getAllData().enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val data = response.body()
                data?.let{

                    Log.d("check data",data.toString())
                    adapter.setData(it.infoGempa.gempa)



                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.e("failure detected",t.toString())
            }
        })

    }
}
package com.hilmihanif.infomasigempa

import com.hilmihanif.infomasigempa.adapter.NetworkConfig
import com.hilmihanif.infomasigempa.data.Result
import android.content.Intent
import android.icu.text.IDNA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hilmihanif.infomasigempa.adapter.GempaAdapter
import com.hilmihanif.infomasigempa.data.Gempa
import com.hilmihanif.infomasigempa.data.InfoGempa
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GempaListActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_LIST_GEMPA ="EXTRA_LIST_GEMPA"
    }

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

        val resultGempa  = intent.getParcelableExtra<InfoGempa>(EXTRA_LIST_GEMPA)

        resultGempa?.let{
            rvGempa.adapter = adapter
            adapter.setData(it.gempa)
        }





    }
}
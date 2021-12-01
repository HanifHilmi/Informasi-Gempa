package com.hilmihanif.infomasigempa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.hilmihanif.infomasigempa.data.Gempa


class DetailActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_GEMPA:String = "EXTRA_GEMPA"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val tvWilayahDetail = findViewById<TextView>(R.id.tv_wilayahdetail)
        val tvWaktuDetail = findViewById<TextView>(R.id.tv_waktudetail)
        val tvMagnitudeDetail = findViewById<TextView>(R.id.tv_magnitudedetail)
        val tvKedalamanDetail = findViewById<TextView>(R.id.tv_kedalamandetail)
        val tvLintangBujurDetail = findViewById<TextView>(R.id.tv_lintangbujurdetail)
        val tvPotensiDetail = findViewById<TextView>(R.id.tv_potensidetail)
        val ivShakeMap = findViewById<ImageView>(R.id.iv_shakemapdetail)

        val dataGempa = intent.getParcelableExtra<Gempa>(EXTRA_GEMPA)


        dataGempa?.let {
            tvWilayahDetail.text = it.wilayah
            tvKedalamanDetail.text = "Kedalaman: ${it.kedalaman}"
            tvWaktuDetail.text = "Waktu: ${it.tanggal} pukul ${it.jam} "
            tvLintangBujurDetail.text = "Koordinat: ${it.lintang} ${it.bujur}"
            tvMagnitudeDetail.text ="Magnitude: ${it.magnitude}"

            tvPotensiDetail.text = it.potensi
            //2021 11 30 06 03 17
            val number="${it.dateTime.subSequence(0,4)}${it.dateTime.subSequence(5,7)}${it.dateTime.subSequence(8,10)}"+"${it.jam.subSequence(0,2)}${it.jam.subSequence(3,5)}${it.jam.subSequence(6,8)}"
            val shakemapUrl = "https://data.bmkg.go.id/DataMKG/TEWS/${number}.mmi.jpg"

            ivShakeMap.load(shakemapUrl){
                placeholder(R.drawable.ic_image_search)
                error(R.drawable.ic_image_notfound)
            }

        }
    }
}

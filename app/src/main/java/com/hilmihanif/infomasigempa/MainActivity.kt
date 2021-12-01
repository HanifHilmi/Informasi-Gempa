package com.hilmihanif.infomasigempa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.hilmihanif.infomasigempa.data.Gempa

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_dataLengkap = findViewById<Button>(R.id.btn_dataLengkap)

        btn_dataLengkap.setOnClickListener() {
            val intent = Intent(this, GempaListActivity::class.java)
            startActivity(intent)
        }


    }
}
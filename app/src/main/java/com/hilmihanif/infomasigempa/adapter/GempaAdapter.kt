package com.hilmihanif.infomasigempa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hilmihanif.infomasigempa.R
import com.hilmihanif.infomasigempa.data.Gempa

class GempaAdapter(
    val onClickListener:(Gempa)->Unit
) : RecyclerView.Adapter<GempaAdapter.GempaViewHolder>() {
    val listGempa: MutableList<Gempa> = mutableListOf()

    fun setData(gempaList: List<Gempa>) {
        listGempa.addAll(gempaList)
        notifyDataSetChanged()
    }

    inner class GempaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvWilayah = itemView.findViewById<TextView>(R.id.tv_wilayahItem )
        val tvDeskripsi = itemView.findViewById<TextView>(R.id.tv_deskripsiItem)
        val tvPotensi = itemView.findViewById<TextView>(R.id.tv_potensigempaItem)
        val tvMagnitude = itemView.findViewById<TextView>(R.id.tv_magnitudeItem)



        fun bind(gempa: Gempa) {
            tvWilayah.text = gempa.wilayah
            tvDeskripsi.text = "${gempa.tanggal} ${gempa.jam}"
            //if(gempa.potensi.subSequence(0,4).equals("Tidak")){ }
            tvPotensi.text = gempa.potensi

            tvMagnitude.text = gempa.magnitude
            itemView.setOnClickListener{onClickListener(gempa)}

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GempaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_listgempa_layout, parent, false)
        return GempaViewHolder(view)
    }

    override fun onBindViewHolder(holder: GempaViewHolder, position: Int) {
        holder.bind(listGempa[position])
    }

    override fun getItemCount(): Int = listGempa.size


}
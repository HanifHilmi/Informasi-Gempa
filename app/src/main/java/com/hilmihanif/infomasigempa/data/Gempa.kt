package com.hilmihanif.infomasigempa.data


import android.os.Parcelable
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class Gempa(
    @SerializedName("Tanggal")
    val tanggal:String,
    @SerializedName("Jam")
    val jam:String,
    @SerializedName("DateTime")
    val dateTime:String,
    @SerializedName("Coordinates")
    val coordinates:String,
    @SerializedName("Lintang")
    val lintang:String,
    @SerializedName("Bujur")
    val bujur:String,
    @SerializedName("Magnitude")
    val magnitude:String,
    @SerializedName("Kedalaman")
    val kedalaman:String,
    @SerializedName("Wilayah")
    val wilayah:String,
    @SerializedName("Potensi")
    val potensi:String,
):Parcelable {
        companion object{
            var markerCount = 0;
        }
        // contoh data coordinates -7.60,105.86
        fun getLintang():Double{
            var count:Int =0
            for(i:Char in this.coordinates){
                if(i != ','){
                    count++
                }else{
                    break
                }
            }
            val temp = coordinates.subSequence(0,count).toString()
            return temp.toDouble()
        }
        fun getBujur():Double{
            var count:Int =0
            for(i:Char in this.coordinates){
                if(i != ','){
                    count++
                }else{
                    break
                }

            }
            val temp = coordinates.subSequence(count+1,coordinates.length).toString()
            return temp.toDouble()
        }
    fun getLatLong():LatLng{
        return LatLng(this.getLintang(),this.getBujur())
    }
    fun createGempaMarker(map: GoogleMap): Marker? {
        markerCount++
        return map.addMarker(
            MarkerOptions()
            .position(this.getLatLong())
            .title(this.wilayah))
    }
}
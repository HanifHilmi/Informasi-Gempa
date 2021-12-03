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

        fun getLintang():Double{
            var temp:String = ""
            for(i:Char in this.lintang){
                if(i != ' '){
                    temp += i
                }else{
                    return temp.toDouble()
                }
            }
            return 0.0
        }
        fun getBujur():Double{
            var temp:String = ""
            for(i:Char in this.bujur){
                if(i != ' '){
                    temp += i
                }else{
                    return temp.toDouble()
                }
            }
            return 0.0
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
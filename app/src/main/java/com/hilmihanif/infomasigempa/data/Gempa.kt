package com.hilmihanif.infomasigempa.data


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gempa(
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

    ):Parcelable
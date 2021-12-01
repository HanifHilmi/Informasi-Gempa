package com.hilmihanif.infomasigempa.data

import com.google.android.gms.maps.model.LatLng

class KoordinatGempa(
    val wilayah:String,
    val lat: String,
    val long: String
){
    fun getLatDouble():Double{
        var temp:String = ""
        for(i:Char in lat){
            if(i != ' '){
                temp += i
            }else{
                return temp.toDouble()
            }
        }
        return 0.0
    }

    fun getLongDouble():Double{
        var temp:String = ""
        for(i:Char in long){
            if(i != ' '){
                temp += i
            }else{
                return temp.toDouble()
            }
        }
        return 0.0
    }

    fun getLatLong():LatLng{
        return LatLng(getLatDouble(),getLongDouble())
    }
}

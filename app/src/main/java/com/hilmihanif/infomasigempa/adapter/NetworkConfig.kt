package com.hilmihanif.infomasigempa.adapter

import com.hilmihanif.infomasigempa.data.Gempa
import com.hilmihanif.infomasigempa.data.Result
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class NetworkConfig {
    fun getInterceptor(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://data.bmkg.go.id/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getService() = getRetrofit().create(DataGempa::class.java)
}


interface DataGempa{
    @GET("DataMKG/TEWS/gempaterkini.json")
    fun getAllData(): Call<Result>
}
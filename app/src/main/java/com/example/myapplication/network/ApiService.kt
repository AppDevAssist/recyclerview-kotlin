package com.example.myapplication.network

import com.example.myapplication.models.Property
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://simple-node-app-nkd.herokuapp.com/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL).build()

interface ApiService{

    @GET("horizontal")
    fun getAllData(): Call<List<Property>>

}

object Api {
    val retrofitService: ApiService by lazy{retrofit.create(ApiService::class.java)}
}
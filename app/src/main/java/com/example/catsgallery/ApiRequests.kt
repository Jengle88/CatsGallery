package com.example.catsgallery


import com.example.catsgallery.catApi.CatData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiRequests {
    @GET("/catapi/restId/{id}")
    fun getCatData(@Path("id") id: String): Call<CatData>
}
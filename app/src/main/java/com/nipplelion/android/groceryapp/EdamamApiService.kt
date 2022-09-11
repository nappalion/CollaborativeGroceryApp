package com.nipplelion.android.groceryapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EdamamApiService {

    @GET("/api/food-database/v2/parser")
    fun getFood(@Query("app_id") appId: String, @Query("app_key") appKey: String, @Query("upc") upc: String): Call<FoodData>
}
package com.mstar004.catfacts

import com.mstar004.catfacts.api.CatJsonFact
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequest {

    @GET("/facts/random")
    fun getCatFacts():Call<CatJsonFact>

}
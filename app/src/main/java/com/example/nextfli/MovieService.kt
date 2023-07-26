package com.example.nextfli

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET

interface MovieService {

    @GET("/3/search/movie?query=Jack+Reacher&api_key=83e94604d5c1c2bdd952afcabe3b6bfd")
    fun getMovie(): Call<JsonObject>
}
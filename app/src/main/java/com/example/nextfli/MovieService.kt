package com.example.nextfli

import retrofit2.Call
import retrofit2.http.GET

interface MovieService {

    @GET("/3/movie/502356?api_key=83e94604d5c1c2bdd952afcabe3b6bfd")
    fun getMovie(): Call<String>
}
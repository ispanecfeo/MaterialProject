package com.example.materiallesson.api

import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {

    @GET("planetary/apod")
    suspend fun getNasaPicture(
       @Query("api_key") key: String
    ): NasaResponse

}
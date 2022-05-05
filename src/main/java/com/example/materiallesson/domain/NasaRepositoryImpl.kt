package com.example.materiallesson.domain
import com.example.materiallesson.BuildConfig
import com.example.materiallesson.api.NasaApi
import com.example.materiallesson.api.NasaResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NasaRepositoryImpl:NasaRepository {

    private val api = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.nasa.gov/")
        .client(OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
            .build()
        )
        .build()
        .create(NasaApi::class.java)

    override suspend fun getNasaPicture(): NasaResponse =
        api.getNasaPicture(BuildConfig.NASA_API_KEY)




}
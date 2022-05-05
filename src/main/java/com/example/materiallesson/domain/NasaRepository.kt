package com.example.materiallesson.domain

import com.example.materiallesson.api.NasaResponse

interface NasaRepository {
    suspend fun getNasaPicture() : NasaResponse
}
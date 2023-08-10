package com.flasshka.testapis.foximageapi.service

import com.flasshka.testapis.foximageapi.models.FoxImage
import retrofit2.Call
import retrofit2.http.GET

interface FoxImageService {
    @GET("/floof/")
    fun get(): Call<FoxImage>
}
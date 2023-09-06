package com.flasshka.testapis.GptMessenger.services

import com.flasshka.testapis.GptMessenger.models.GptRequestModel
import com.flasshka.testapis.GptMessenger.models.GptResponseModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface GptService {
    @POST("/v1/completions")
    fun post(@Body requestBody: GptRequestModel) : Response<GptResponseModel>
}
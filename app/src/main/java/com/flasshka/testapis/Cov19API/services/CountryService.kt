package com.flasshka.testapis.Cov19API.services

import com.flasshka.testapis.Cov19API.models.MyCountry
import retrofit2.Call
import retrofit2.http.GET

interface CountryService {

    @GET("countries")
    fun getAffectedCountryList () : Call<List<MyCountry>>
}
package com.flasshka.testapis.DeckOfCardsAPI.services

import com.flasshka.testapis.DeckOfCardsAPI.models.CardsImage
import com.flasshka.testapis.DeckOfCardsAPI.models.DeckOfCards
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CardImageService {
    @GET("/api/deck/new/draw/")
    fun get (@Query("count") count: Int) : Call<CardsImage>
}
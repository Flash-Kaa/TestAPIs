package com.flasshka.testapis.DeckOfCardsAPI.services

import com.flasshka.testapis.DeckOfCardsAPI.models.DeckOfCards
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DeckService {
    @GET("/api/deck/new/shuffle/")
    fun get (@Query("deck_count") deck_count: Int) : Call<DeckOfCards>
}
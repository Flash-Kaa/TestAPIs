package com.flasshka.testapis.DeckOfCardsAPI.models

data class DeckOfCards(
    val deck_id: String,
    val remaining: Int,
    val shuffled: Boolean,
    val success: Boolean
)
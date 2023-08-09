package com.flasshka.testapis.DeckOfCardsAPI.models

data class CardsImage(
    val cards: List<Card>,
    val deck_id: String,
    val remaining: Int,
    val success: Boolean
) {
    data class Card(
        val code: String,
        val image: String,
        val images: Images,
        val suit: String,
        val value: String
    ) {
        data class Images(
            val png: String,
            val svg: String
        )
    }
}
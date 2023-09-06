package com.flasshka.testapis.GptMessenger.models

data class GptRequestModel(
    val messages: List<Message>,
    val model: String
)
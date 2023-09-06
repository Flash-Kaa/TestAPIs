package com.flasshka.testapis.GptMessenger.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class Message(
    val content: String,
    val role: String
)
package dev.jeziellago.eventdrivenarch.network

import com.google.gson.annotations.SerializedName

class JokeResponse(
    @SerializedName("value")
    val name: String
)

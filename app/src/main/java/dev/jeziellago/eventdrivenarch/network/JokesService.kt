package dev.jeziellago.eventdrivenarch.network

import retrofit2.http.GET

interface JokesService {
    @GET("jokes/random")
    suspend fun getJoke(): JokeResponse
}
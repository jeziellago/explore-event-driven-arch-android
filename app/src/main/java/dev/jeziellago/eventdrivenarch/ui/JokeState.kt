package dev.jeziellago.eventdrivenarch.ui

data class JokeState(
    val isLoading: Boolean = false,
    val jokes: List<Joke> = emptyList(),
    val error: String? = null
)
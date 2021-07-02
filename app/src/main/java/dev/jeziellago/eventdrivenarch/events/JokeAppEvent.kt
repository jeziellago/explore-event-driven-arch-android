package dev.jeziellago.eventdrivenarch.events

import dev.jeziellago.core.Event
import dev.jeziellago.eventdrivenarch.ui.Joke


sealed class JokeAppEvent : Event {

    object WaitingForNewJoke : JokeAppEvent()

    object NewJokeAvailable : JokeAppEvent()

    data class ErrorOnNewJoke(val message: String) : JokeAppEvent()

    data class JokesChanged(val list: List<Joke>) : JokeAppEvent()
}

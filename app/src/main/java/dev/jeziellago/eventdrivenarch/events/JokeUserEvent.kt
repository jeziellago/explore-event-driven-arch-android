package dev.jeziellago.eventdrivenarch.events

import dev.jeziellago.core.Event
import dev.jeziellago.eventdrivenarch.ui.Joke


sealed class JokeUserEvent : Event {

    object GetJoke : JokeUserEvent()

    object ListJokes : JokeUserEvent()

    sealed class Navigation : JokeUserEvent() {
        data class GoToJokeDetail(val joke: Joke) : Navigation()
    }
}

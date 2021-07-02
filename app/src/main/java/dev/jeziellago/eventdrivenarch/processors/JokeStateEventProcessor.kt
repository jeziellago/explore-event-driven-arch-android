package dev.jeziellago.eventdrivenarch.processors

import androidx.lifecycle.viewModelScope
import dev.jeziellago.core.Broker
import dev.jeziellago.core.InternalEventProcessorApi
import dev.jeziellago.core.bindEventProcessor
import dev.jeziellago.eventdrivenarch.events.JokeAppEvent
import dev.jeziellago.eventdrivenarch.events.JokeUserEvent
import dev.jeziellago.eventdrivenarch.ext.changeState
import dev.jeziellago.eventdrivenarch.ui.JokeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JokeStateEventProcessor(
    private val broker: Broker
) : AndroidEventProcessor<JokeAppEvent>() {

    private val jokeState = MutableStateFlow(JokeState())
    val state: StateFlow<JokeState>
        get() = jokeState

    init {
        broker.bindEventProcessor(
            eventType = JokeAppEvent::class,
            eventProcessor = this
        )

        viewModelScope.launch {
            broker.sendEvent(JokeUserEvent.ListJokes)
        }
    }

    @InternalEventProcessorApi
    override suspend fun process(event: JokeAppEvent) {
        when (event) {
            is JokeAppEvent.WaitingForNewJoke -> {
                jokeState.changeState { copy(isLoading = true) }
            }
            is JokeAppEvent.NewJokeAvailable -> {
                jokeState.changeState { copy(isLoading = false) }
            }
            is JokeAppEvent.ErrorOnNewJoke -> {
                jokeState.changeState { copy(isLoading = false, error = event.message) }
            }
            is JokeAppEvent.JokesChanged -> {
                jokeState.changeState { copy(jokes = event.list, error = null) }
            }
        }
    }
}

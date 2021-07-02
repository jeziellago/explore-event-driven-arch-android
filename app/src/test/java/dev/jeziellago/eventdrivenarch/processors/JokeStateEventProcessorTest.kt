package dev.jeziellago.eventdrivenarch.processors

import dev.jeziellago.eventdrivenarch.CoroutineTestRule
import dev.jeziellago.eventdrivenarch.FakeBrokerWithSingleProcessor
import dev.jeziellago.eventdrivenarch.events.JokeAppEvent
import dev.jeziellago.eventdrivenarch.events.JokeUserEvent
import dev.jeziellago.eventdrivenarch.ui.Joke
import dev.jeziellago.eventdrivenarch.ui.JokeState
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class JokeStateEventProcessorTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @Test
    fun `on EventProcessor initialization Should render JokeState with jokes`() {
        val fakeBroker = FakeBrokerWithSingleProcessor { JokeAppEvent.JokesChanged(list = jokes()) }
        val stateEventProcessor = JokeStateEventProcessor(fakeBroker)
        fakeBroker.register(JokeAppEvent::class, stateEventProcessor)
        val expectedState = JokeState(jokes = jokes())

        val jokeState: JokeState = stateEventProcessor.state.value

        assertEquals(expectedState, jokeState)
    }

    @Test
    fun `sendEvent GetJoke with error result Should render JokeState with error message`() =
        runBlocking {
            val fakeBroker = FakeBrokerWithSingleProcessor { JokeAppEvent.ErrorOnNewJoke("Error!") }
            val stateEventProcessor = JokeStateEventProcessor(fakeBroker)
            fakeBroker.register(JokeAppEvent::class, stateEventProcessor)
            val expectedState = JokeState(error = "Error!")

            fakeBroker.sendEvent(JokeUserEvent.GetJoke)

            val jokeState: JokeState = stateEventProcessor.state.value

            assertEquals(expectedState, jokeState)
        }

    private fun jokes() = listOf(Joke("a"), Joke("b"))
}

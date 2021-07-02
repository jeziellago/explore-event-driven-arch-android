package dev.jeziellago.eventdrivenarch.processors

import dev.jeziellago.core.Broker
import dev.jeziellago.core.Event
import dev.jeziellago.eventdrivenarch.FakeBrokerWithEventRecord
import dev.jeziellago.eventdrivenarch.events.JokeAppEvent
import dev.jeziellago.eventdrivenarch.events.JokeUserEvent
import dev.jeziellago.eventdrivenarch.localdata.dao.JokeDao
import dev.jeziellago.eventdrivenarch.localdata.model.Joke
import dev.jeziellago.eventdrivenarch.network.JokeResponse
import dev.jeziellago.eventdrivenarch.network.JokesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.Exception

class GetJokeEventProcessorTest {

    @Test
    fun `when process with success Should to send WaitingForNewJoke and NewJokeAvailable`() =
        runBlocking {
            val fakeBroker = FakeBrokerWithEventRecord()
            val getJokeEventProcessor = GetJokeEventProcessor(
                broker = fakeBroker,
                jokesService = createFakeJokeService { JokeResponse("joke!") },
                jokeDao = createFakeJokeDao()
            )

            val expectedEvents = listOf(
                JokeAppEvent.WaitingForNewJoke,
                JokeAppEvent.NewJokeAvailable
            )

            getJokeEventProcessor.process(JokeUserEvent.GetJoke)

            assertEquals(expectedEvents, fakeBroker.receivedEvents)
        }

    @Test
    fun `when process with error Should to send WaitingForNewJoke and NewJokeAvailable`() =
        runBlocking {
            val fakeBroker = FakeBrokerWithEventRecord()
            val getJokeEventProcessor = GetJokeEventProcessor(
                broker = fakeBroker,
                jokesService = createFakeJokeService { throw Exception() },
                jokeDao = createFakeJokeDao()
            )

            val expectedEvents = listOf(
                JokeAppEvent.WaitingForNewJoke,
                JokeAppEvent.ErrorOnNewJoke("Sorry, we have a problem :(")
            )

            getJokeEventProcessor.process(JokeUserEvent.GetJoke)

            assertEquals(expectedEvents, fakeBroker.receivedEvents)
        }

    private fun createFakeJokeService(response: () -> JokeResponse) = object : JokesService {
        override suspend fun getJoke(): JokeResponse {
            return response()
        }
    }

    private fun createFakeJokeDao() = object : JokeDao {
        override fun getAll(): Flow<List<Joke>> = emptyFlow()
        override suspend fun insert(joke: Joke) {}
    }
}

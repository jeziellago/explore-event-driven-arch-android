package dev.jeziellago.eventdrivenarch.processors

import dev.jeziellago.core.Broker
import dev.jeziellago.core.EventProcessor
import dev.jeziellago.eventdrivenarch.localdata.dao.JokeDao
import dev.jeziellago.eventdrivenarch.localdata.model.Joke
import dev.jeziellago.eventdrivenarch.events.JokeAppEvent
import dev.jeziellago.eventdrivenarch.events.JokeUserEvent
import dev.jeziellago.eventdrivenarch.network.JokesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GetJokeEventProcessor(
    private val broker: Broker,
    private val jokesService: JokesService,
    private val jokeDao: JokeDao
) : EventProcessor<JokeUserEvent.GetJoke> {

    override suspend fun process(event: JokeUserEvent.GetJoke) {
        broker.sendEvent(JokeAppEvent.WaitingForNewJoke)
        runCatching {
            val response = withContext(Dispatchers.IO) { jokesService.getJoke() }
            jokeDao.insert(Joke(name = response.name))
        }.onSuccess {
            broker.sendEvent(JokeAppEvent.NewJokeAvailable)
        }.onFailure {
            broker.sendEvent(JokeAppEvent.ErrorOnNewJoke("Sorry, we have a problem :("))
        }
    }
}

package dev.jeziellago.eventdrivenarch.processors

import dev.jeziellago.core.Broker
import dev.jeziellago.core.EventProcessor
import dev.jeziellago.eventdrivenarch.localdata.dao.JokeDao
import dev.jeziellago.eventdrivenarch.events.JokeAppEvent
import dev.jeziellago.eventdrivenarch.events.JokeUserEvent
import dev.jeziellago.eventdrivenarch.ui.Joke
import kotlinx.coroutines.flow.collect

class ListJokesEventProcessor(
    private val broker: Broker,
    private val jokeDao: JokeDao
) : EventProcessor<JokeUserEvent.ListJokes> {

    override suspend fun process(event: JokeUserEvent.ListJokes) {
        jokeDao.getAll()
            .collect { jokes ->
                broker.sendEvent(JokeAppEvent.JokesChanged(list = jokes.map { Joke(it.name) }))
            }
    }
}

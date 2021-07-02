package dev.jeziellago.eventdrivenarch.di

import android.content.Context
import androidx.room.Room
import dev.jeziellago.core.brokerOf
import dev.jeziellago.eventdrivenarch.localdata.JokeDatabase
import dev.jeziellago.eventdrivenarch.di.Injector.get
import dev.jeziellago.eventdrivenarch.di.Injector.provides
import dev.jeziellago.eventdrivenarch.events.JokeUserEvent
import dev.jeziellago.eventdrivenarch.network.HttpClient
import dev.jeziellago.eventdrivenarch.network.JokesService
import dev.jeziellago.eventdrivenarch.network.createHttpClient
import dev.jeziellago.eventdrivenarch.processors.GetJokeEventProcessor
import dev.jeziellago.eventdrivenarch.processors.JokeStateEventProcessor
import dev.jeziellago.eventdrivenarch.processors.ListJokesEventProcessor

val appModule = { context: Context ->
    provides(singleton = true) {
        Room.databaseBuilder(context, JokeDatabase::class.java, "jokes.db").build()
    }
    provides(singleton = true) { createHttpClient() }
}

val jokeModule = {
    provides { get<HttpClient>().create(JokesService::class) }
    provides { get<JokeDatabase>().getJokeDao() }
    provides { JokeStateEventProcessor(broker = get()) }
    provides(singleton = true) {
        brokerOf { broker ->
            register(
                eventType = JokeUserEvent.GetJoke::class,
                eventProcessor = GetJokeEventProcessor(
                    broker = broker,
                    jokesService = get(),
                    jokeDao = get()
                )
            )
            register(
                eventType = JokeUserEvent.ListJokes::class,
                eventProcessor = ListJokesEventProcessor(broker = broker, jokeDao = get())
            )
        }
    }
}
package dev.jeziellago.core

interface EventProcessor<E : Event> {
    suspend fun process(event: E)
}

package dev.jeziellago.core

import kotlin.reflect.KClass

abstract class EventProcessorRegister {

    protected val eventProcessors = mutableMapOf<KClass<*>, EventProcessor<Event>>()

    fun <T : Event> register(eventType: KClass<T>, eventProcessor: EventProcessor<T>) = apply {
        @Suppress("UNCHECKED_CAST")
        eventProcessors[eventType] = eventProcessor as EventProcessor<Event>
    }
}
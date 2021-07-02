package dev.jeziellago.eventdrivenarch

import dev.jeziellago.core.Broker
import dev.jeziellago.core.Event
import dev.jeziellago.core.EventProcessorRegister

class FakeBrokerWithSingleProcessor(
    private val eventProvider: () -> Event
) : EventProcessorRegister(), Broker {

    override suspend fun <T : Event> sendEvent(event: T) {
        eventProcessors.values.firstOrNull()?.process(eventProvider())
    }
}

class FakeBrokerWithEventRecord : Broker {
    val receivedEvents = mutableListOf<Event>()
    override suspend fun <T : Event> sendEvent(event: T) {
        receivedEvents.add(event)
    }
}

package dev.jeziellago.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface Broker {
    suspend fun <T : Event> sendEvent(event: T)
}

fun brokerOf(register: EventProcessorRegister.(Broker) -> Unit = {}): Broker = RealBroker()
    .apply { register(this) }

fun  <T : Event> Broker.sendEventOnScope(coroutineScope: CoroutineScope, event: T) {
    coroutineScope.launch {
        sendEvent(event)
    }
}

internal class RealBroker : EventProcessorRegister(), Broker {

    override suspend fun <T : Event> sendEvent(event: T) {
        val eventProcessorKey = event::class
        val eventProcessor = eventProcessors[eventProcessorKey] ?: run {
            val childProcessorKey = eventProcessors.keys.firstOrNull { it.isInstance(event) }
            eventProcessors[childProcessorKey]
        }
        eventProcessor?.process(event)
    }
}

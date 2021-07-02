package dev.jeziellago.core

import kotlin.reflect.KClass

fun <T: Event> Broker.bindEventProcessor(eventType: KClass<T>, eventProcessor: EventProcessor<T>) {
    (this as EventProcessorRegister).register(eventType, eventProcessor)
}

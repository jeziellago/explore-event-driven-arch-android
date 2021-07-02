package dev.jeziellago.eventdrivenarch.ext

import kotlinx.coroutines.flow.MutableStateFlow

fun <T: Any> MutableStateFlow<T>.changeState(onChange: T.() -> T) {
    value = onChange(value)
}

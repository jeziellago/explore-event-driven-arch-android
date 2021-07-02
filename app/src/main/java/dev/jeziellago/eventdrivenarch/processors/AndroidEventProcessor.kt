package dev.jeziellago.eventdrivenarch.processors

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dev.jeziellago.core.Event
import dev.jeziellago.core.EventProcessor
import dev.jeziellago.eventdrivenarch.di.Injector

abstract class AndroidEventProcessor<T : Event> : ViewModel(), EventProcessor<T>

inline fun <reified T : AndroidEventProcessor<*>>
        ViewModelStoreOwner.injectAndroidEventProcessor() =
    lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return Injector.get(modelClass.kotlin)
            }
        }).get(T::class.java)
    }

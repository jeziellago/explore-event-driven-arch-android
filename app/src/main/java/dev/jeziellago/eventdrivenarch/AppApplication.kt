package dev.jeziellago.eventdrivenarch

import android.app.Application
import dev.jeziellago.eventdrivenarch.di.appModule
import dev.jeziellago.eventdrivenarch.di.jokeModule

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appModule(this)
        jokeModule()
    }
}
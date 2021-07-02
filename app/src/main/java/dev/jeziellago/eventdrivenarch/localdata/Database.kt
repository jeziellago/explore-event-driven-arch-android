package dev.jeziellago.eventdrivenarch.localdata

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.jeziellago.eventdrivenarch.localdata.dao.JokeDao
import dev.jeziellago.eventdrivenarch.localdata.model.Joke


@Database(entities = [Joke::class], version = 1)
abstract class JokeDatabase : RoomDatabase() {
    abstract fun getJokeDao(): JokeDao
}

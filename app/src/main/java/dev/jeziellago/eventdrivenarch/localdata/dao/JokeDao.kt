package dev.jeziellago.eventdrivenarch.localdata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import dev.jeziellago.eventdrivenarch.localdata.model.Joke
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDao {
    @Query("SELECT * FROM joke")
    fun getAll(): Flow<List<Joke>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(joke: Joke)
}

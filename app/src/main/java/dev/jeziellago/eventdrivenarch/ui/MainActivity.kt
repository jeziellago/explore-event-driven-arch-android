package dev.jeziellago.eventdrivenarch.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.jeziellago.core.Broker
import dev.jeziellago.core.EventProcessor
import dev.jeziellago.core.bindEventProcessor
import dev.jeziellago.core.sendEventOnScope
import dev.jeziellago.eventdrivenarch.databinding.ActivityMainBinding
import dev.jeziellago.eventdrivenarch.di.Injector
import dev.jeziellago.eventdrivenarch.events.JokeUserEvent.GetJoke
import dev.jeziellago.eventdrivenarch.events.JokeUserEvent.Navigation
import dev.jeziellago.eventdrivenarch.processors.JokeStateEventProcessor
import dev.jeziellago.eventdrivenarch.processors.injectAndroidEventProcessor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), EventProcessor<Navigation> {

    private val broker: Broker by lazy { Injector.get() }
    private val jokeEventProcessor: JokeStateEventProcessor by injectAndroidEventProcessor()

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater).apply { setContentView(root) }
    }

    private val jokesAdapter by lazy {
        JokesAdapter { joke ->
            broker.sendEventOnScope(lifecycleScope, Navigation.GoToJokeDetail(joke))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        broker.bindEventProcessor(Navigation::class, this)
        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            jokesRecyclerView.adapter = jokesAdapter
            btnGetNewJoke.setOnClickListener {
                broker.sendEventOnScope(lifecycleScope, GetJoke)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                jokeEventProcessor.state
                    .collect { state -> renderState(state) }
            }
        }
    }

    private fun renderState(state: JokeState) {
        binding.progressBar.isVisible = state.isLoading

        state.jokes
            .takeIf { it.isNotEmpty() }
            .run { jokesAdapter.submitList(state.jokes) }

        if (state.error != null) {
            Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
        }
    }

    override suspend fun process(event: Navigation) {
        when (event) {
            is Navigation.GoToJokeDetail -> {
                AlertDialog.Builder(this)
                    .setMessage(event.joke.content)
                    .show()
            }
        }
    }
}

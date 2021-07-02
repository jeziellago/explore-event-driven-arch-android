package dev.jeziellago.eventdrivenarch.di

import kotlin.reflect.KClass

data class Provider<T : Any>(
    val singleton: Boolean = false,
    val factory: () -> T
)

object Injector {

    @PublishedApi
    internal val providers = mutableMapOf<KClass<*>, Provider<Any>>()

    @PublishedApi
    internal val singletons = mutableMapOf<KClass<*>, Any>()

    inline fun <reified T : Any> get(): T = get(T::class)

    fun <T : Any> get(depKey: KClass<T>): T {
        val provider = checkNotNull(providers[depKey]) {
            "Definition not found to ${depKey.simpleName}"
        }
        val value = if (provider.singleton) {
            singletons.getOrPut(depKey) { provider.factory() }
        } else {
            provider.factory()
        }
        return value as T
    }

    inline fun <reified T : Any> provides(
        singleton: Boolean = false,
        noinline provider: () -> T
    ) {
        providers[T::class] = Provider(singleton, provider)
    }
}

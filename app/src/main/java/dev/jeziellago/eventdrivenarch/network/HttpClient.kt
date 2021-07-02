package dev.jeziellago.eventdrivenarch.network

import dev.jeziellago.eventdrivenarch.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

interface HttpClient {
    fun <T : Any> create(service: KClass<T>): T
}

fun createHttpClient(retrofit: Retrofit = createRetrofit()): HttpClient {
    return object : HttpClient {
        override fun <T : Any> create(service: KClass<T>): T {
            return retrofit.create(service.java)
        }
    }
}

internal fun createRetrofit(
    baseUrl: String = "https://api.chucknorris.io/",
    okHttpClient: OkHttpClient = createOkHttpClient()
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

internal fun createOkHttpClient(): OkHttpClient {
    val logLevel = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }
    val loggingInterceptor = HttpLoggingInterceptor().apply { level = logLevel }
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}

package com.medioka.guideline.app.data.di

import android.content.Context
import com.medioka.guideline.app.data.remote.WeatherApi
import com.medioka.guideline.app.data.repository.WeatherRepositoryImpl
import com.medioka.guideline.app.domain.repository.WeatherRepository
import com.chuckerteam.chucker.api.ChuckerInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

private const val NETWORK_TIMEOUT_SECONDS = 10L

/**
 * Data layer Koin module.
 * Provides data sources, repository implementations, and mappers.
 */
object WeatherDataModule {

    /** Provides a Koin module for the data layer using DSL */
    val module = module {
        /** Provides Json instance for serialization */
        singleOf(::provideJson)

        singleOf(::provideOkHttpClient)

        /** Provides Retrofit instance configured for the weather API */
        singleOf(::provideRetrofit)

        /** Provides WeatherApi instance */
        singleOf(::provideWeatherApi)

        /** Provides WeatherRepository implementation */
        singleOf(::WeatherRepositoryImpl) bind WeatherRepository::class
    }

    /** Provides a Json instance (Kotlinx serialization) */
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    /** Provides a Retrofit instance configured for the weather API */
    fun provideOkHttpClient(context: Context): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .callTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addNetworkInterceptor(ChuckerInterceptor(context))
        .build()

    fun provideRetrofit(json: Json, okHttpClient: OkHttpClient): retrofit2.Retrofit =
        retrofit2.Retrofit.Builder()
            .baseUrl(WeatherApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    /** Provides WeatherApi instance */
    fun provideWeatherApi(retrofit: retrofit2.Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}

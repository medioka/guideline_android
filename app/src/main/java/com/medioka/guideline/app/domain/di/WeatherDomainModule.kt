package com.medioka.guideline.app.domain.di

import com.medioka.guideline.app.domain.usecase.GetWeatherUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Domain layer Koin module.
 * Provides use cases and repository interfaces.
 */
object WeatherDomainModule {

    /** Provides a Koin module for the domain layer using DSL */
    val module = module {
        /** Provides GetWeatherUseCase instance */
        singleOf(::GetWeatherUseCase)
    }
}
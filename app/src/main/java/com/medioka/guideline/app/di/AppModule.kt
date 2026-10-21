package com.medioka.guideline.app.di

import com.medioka.guideline.app.data.di.WeatherDataModule
import com.medioka.guideline.app.domain.di.WeatherDomainModule
import com.medioka.guideline.app.presentation.home.di.HomeModule
import org.koin.dsl.module

/**
 * Application-level composition root.
 * Composes all layer modules (data, domain) into a single Koin graph.
 *
 * Previously used @Module/@Single/@Factory annotations.
 * Now uses Koin DSL with singleOf/factoryOf from per-layer modules.
 */
object AppModule {

    /** Provides the complete Koin module by composing all layer modules. */
    val module = module {
        includes(
            WeatherDataModule.module,
            WeatherDomainModule.module,
            HomeModule.module,
        )
    }
}

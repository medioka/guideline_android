package com.medioka.guideline.app.di

/**
 * Koin entry point marker object.
 * The actual Koin graph is configured in GuidelineApplication.startKoin { } using AppModule.module.
 *
 * Previously used @KoinApplication(modules = [AppModule::class]) with KSP annotations.
 * Now using Koin DSL with singleOf/factoryOf.
 */
object WeatherKoinApplication

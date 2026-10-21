package com.medioka.guideline.app.di

import com.medioka.guideline.app.domain.repository.WeatherRepository
import com.medioka.guideline.app.domain.usecase.GetWeatherUseCase
import okhttp3.OkHttpClient
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class AppModuleTest {

    @Test
    fun `app graph resolves repository and use case`() {
        val koin = koinApplication {
            modules(
                AppModule.module,
                module { single { OkHttpClient.Builder().build() } },
            )
        }.koin

        try {
            assertNotNull(koin.get<WeatherRepository>())
            assertNotNull(koin.get<GetWeatherUseCase>())
        } finally {
            koin.close()
        }
    }

}

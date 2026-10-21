package com.medioka.guideline.app.presentation.home.di

import com.medioka.guideline.app.presentation.common.location.LocationHelper
import com.medioka.guideline.app.presentation.home.HomeViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object HomeModule {
    val module = module {
        viewModelOf(::HomeViewModel)
        singleOf(::LocationHelper)
    }
}

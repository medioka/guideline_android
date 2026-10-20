package com.medioka.guideline.app

import android.app.Application
import com.medioka.guideline.app.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GuidelineApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Use Koin DSL with modular structure (data + domain layers)
        startKoin {
            androidContext(this@GuidelineApplication)
            modules(AppModule.module)
        }
    }
}
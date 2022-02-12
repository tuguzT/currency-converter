package io.github.tuguzT.currencyconverter.presentation

import android.app.Application
import io.github.tuguzT.currencyconverter.di.appModule
import io.github.tuguzT.currencyconverter.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused")
class CurrencyConverterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(applicationContext)
            modules(appModule, networkModule)
        }
    }
}

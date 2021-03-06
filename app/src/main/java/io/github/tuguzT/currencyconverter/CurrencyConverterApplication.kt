package io.github.tuguzT.currencyconverter

import android.app.Application
import io.github.tuguzT.currencyconverter.di.appModule
import io.github.tuguzT.currencyconverter.di.networkModule
import io.github.tuguzT.currencyconverter.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@Suppress("unused")
class CurrencyConverterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(applicationContext)
            modules(appModule, repositoryModule, networkModule)
        }
    }
}

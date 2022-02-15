package io.github.tuguzT.currencyconverter.di

import android.content.Context
import androidx.room.Room
import io.github.tuguzT.currencyconverter.repository.room.ConversionRateRepository
import io.github.tuguzT.currencyconverter.repository.room.CurrencyConverterDatabase
import io.github.tuguzT.currencyconverter.repository.room.SupportedCodeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single { database(androidContext()) }
    single { supportedCodeRepository(get()) }
    single { conversionRateRepository(get()) }
}

private fun database(context: Context) =
    Room.databaseBuilder(
        context.applicationContext,
        CurrencyConverterDatabase::class.java,
        "currency_converter",
    ).build()

private fun supportedCodeRepository(database: CurrencyConverterDatabase): SupportedCodeRepository =
    SupportedCodeRepository(database)

private fun conversionRateRepository(database: CurrencyConverterDatabase): ConversionRateRepository =
    ConversionRateRepository(database)

package io.github.tuguzT.currencyconverter.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.github.tuguzT.currencyconverter.repository.ExchangeRateAPI
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create

val networkModule = module {
    @OptIn(ExperimentalSerializationApi::class)
    single {
        val json: Json = get()
        json.asConverterFactory(MediaType.get("application/json"))
    }

    // Exchange Rate API
    single { retrofit(ExchangeRateAPI.baseURI, get()) }
    single { exchangeRateAPI(get()) }
}

@Suppress("SameParameterValue")
private fun retrofit(baseUrl: String, converterFactory: Converter.Factory): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .build()

private fun exchangeRateAPI(retrofit: Retrofit): ExchangeRateAPI = retrofit.create()

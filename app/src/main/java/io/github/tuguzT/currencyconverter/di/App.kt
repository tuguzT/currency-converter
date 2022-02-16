package io.github.tuguzT.currencyconverter.di

import io.github.tuguzT.currencyconverter.viewmodel.ConverterViewModel
import io.github.tuguzT.currencyconverter.viewmodel.SupportedCodesViewModel
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { json() }
    viewModel { ConverterViewModel(get(), get()) }
    viewModel { SupportedCodesViewModel(get(), get(), get()) }
}

private fun json() = Json { ignoreUnknownKeys = true }

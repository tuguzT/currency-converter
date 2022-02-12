package io.github.tuguzT.currencyconverter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import io.github.tuguzT.currencyconverter.repository.ExchangeRateAPI

class MainActivityModel(private val exchangeRateAPI: ExchangeRateAPI) : ViewModel()

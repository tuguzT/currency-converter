package io.github.tuguzT.currencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.tuguzT.currencyconverter.model.SupportedCode
import io.github.tuguzT.currencyconverter.repository.net.ApiResponse
import io.github.tuguzT.currencyconverter.repository.net.ExchangeRateAPI
import retrofit2.Response

class ConverterViewModel(private val exchangeRateAPI: ExchangeRateAPI) : ViewModel() {
    var baseCode: SupportedCode? = null
    var targetCode: SupportedCode? = null

    fun swapCodes() {
        targetCode = baseCode.also { baseCode = targetCode }
    }

    suspend fun convert(amount: Double): ApiResponse<Double> {
        val base = checkNotNull(baseCode).code
        val target = checkNotNull(targetCode).code
        return when (val result = exchangeRateAPI.latestData(base)) {
            is NetworkResponse.Success -> {
                val rate = checkNotNull(result.body.conversionRates[target])
                NetworkResponse.Success((rate * amount).round(6), Response.success(null))
            }
            else -> {
                @Suppress("UNCHECKED_CAST")
                result as ApiResponse<Double>
            }
        }
    }
}

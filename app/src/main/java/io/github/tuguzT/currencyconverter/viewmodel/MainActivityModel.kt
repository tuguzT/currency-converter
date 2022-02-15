package io.github.tuguzT.currencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.tuguzT.currencyconverter.model.SupportedCode
import io.github.tuguzT.currencyconverter.repository.ApiResponse
import io.github.tuguzT.currencyconverter.repository.ExchangeRateAPI
import io.github.tuguzT.currencyconverter.repository.model.parse
import retrofit2.Response

class MainActivityModel(private val exchangeRateAPI: ExchangeRateAPI) : ViewModel() {
    private var supportedCodes: List<SupportedCode>? = null

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

    suspend fun getSupportedCodes(): ApiResponse<List<SupportedCode>> =
        when (val codes = supportedCodes) {
            null -> updateSupportedCodes()
            else -> NetworkResponse.Success(codes, Response.success(null))
        }

    suspend fun updateSupportedCodes(): ApiResponse<List<SupportedCode>> =
        when (val result = exchangeRateAPI.supportedCodes()) {
            is NetworkResponse.Success -> {
                val parsed = result.body.parse()
                supportedCodes = parsed
                NetworkResponse.Success(parsed, Response.success(null))
            }
            else -> {
                @Suppress("UNCHECKED_CAST")
                result as ApiResponse<List<SupportedCode>>
            }
        }
}

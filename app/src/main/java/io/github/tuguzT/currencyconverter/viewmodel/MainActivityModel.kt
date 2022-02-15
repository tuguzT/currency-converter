package io.github.tuguzT.currencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.tuguzT.currencyconverter.model.SupportedCode
import io.github.tuguzT.currencyconverter.repository.*
import retrofit2.Response

class MainActivityModel(private val exchangeRateAPI: ExchangeRateAPI) : ViewModel() {
    private var supportedCodes: List<SupportedCode>? = null

    var baseCode: SupportedCode? = null
    var targetCode: SupportedCode? = null

    suspend fun convert(amount: Double): ApiResponse<PairConversionResult> {
        val base = checkNotNull(baseCode).code
        val target = checkNotNull(targetCode).code
        return exchangeRateAPI.pairConversion(base, target, amount)
    }

    suspend fun getSupportedCodes(): ApiResponse<List<SupportedCode>> =
        when (val codes = supportedCodes) {
            null -> updateSupportedCodes()
            else -> NetworkResponse.Success(codes, Response.success(Unit))
        }

    suspend fun updateSupportedCodes(): ApiResponse<List<SupportedCode>> =
        when (val result = exchangeRateAPI.supportedCodes()) {
            is NetworkResponse.Success -> {
                val parsed = result.body.parse()
                supportedCodes = parsed
                NetworkResponse.Success(parsed, result.response)
            }
            else -> {
                @Suppress("UNCHECKED_CAST")
                result as NetworkResponse<List<SupportedCode>, ApiError>
            }
        }
}

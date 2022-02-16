package io.github.tuguzT.currencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.tuguzT.currencyconverter.model.SupportedCode
import io.github.tuguzT.currencyconverter.repository.net.ApiResponse
import io.github.tuguzT.currencyconverter.repository.net.ExchangeRateAPI
import io.github.tuguzT.currencyconverter.repository.net.model.parse
import retrofit2.Response

class SupportedCodesListViewModel(private val exchangeRateAPI: ExchangeRateAPI) : ViewModel() {
    private var _supportedCodes: List<SupportedCode>? = null
    val supportedCodes: List<SupportedCode>? get() = _supportedCodes

    suspend fun refreshSupportedCodes(): ApiResponse<List<SupportedCode>> =
        when (val result = exchangeRateAPI.supportedCodes()) {
            is NetworkResponse.Success -> {
                val parsed = result.body.parse()
                _supportedCodes = parsed
                NetworkResponse.Success(parsed, Response.success(null))
            }
            else -> {
                @Suppress("UNCHECKED_CAST")
                result as ApiResponse<List<SupportedCode>>
            }
        }
}

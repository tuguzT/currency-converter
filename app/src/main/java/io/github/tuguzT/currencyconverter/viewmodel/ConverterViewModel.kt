package io.github.tuguzT.currencyconverter.viewmodel

import androidx.lifecycle.ViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import io.github.tuguzT.currencyconverter.model.ConversionRate
import io.github.tuguzT.currencyconverter.model.SupportedCode
import io.github.tuguzT.currencyconverter.repository.net.ApiResponse
import io.github.tuguzT.currencyconverter.repository.net.ExchangeRateAPI
import io.github.tuguzT.currencyconverter.repository.room.ConversionRateRepository
import io.github.tuguzT.currencyconverter.repository.room.inverted
import io.github.tuguzT.currencyconverter.repository.room.toDtos
import io.github.tuguzT.currencyconverter.repository.room.toModel
import retrofit2.Response

class ConverterViewModel(
    private val exchangeRateAPI: ExchangeRateAPI,
    private val conversionRateRepository: ConversionRateRepository,
) : ViewModel() {
    var baseCode: SupportedCode? = null
    var targetCode: SupportedCode? = null

    fun swapCodes() {
        targetCode = baseCode.also { baseCode = targetCode }
    }

    suspend fun getRate(): ApiResponse<ConversionRate> {
        val base = checkNotNull(baseCode).code
        val target = checkNotNull(targetCode).code

        val rate = conversionRateRepository.findById(base to target)?.toModel()
        if (rate != null)
            return NetworkResponse.Success(rate, Response.success(null))

        val newRate = conversionRateRepository.findById(target to base)?.toModel()
        if (newRate != null)
            return NetworkResponse.Success(newRate.inverted(), Response.success(null))

        return when (val result = exchangeRateAPI.latestData(base)) {
            is NetworkResponse.Success -> {
                val rateDtos = result.body.toDtos()
                rateDtos.forEach { conversionRateRepository.save(it) }

                val netRate = checkNotNull(rateDtos.find { it.targetCode == target })
                NetworkResponse.Success(netRate.toModel(), Response.success(null))
            }
            else -> {
                @Suppress("UNCHECKED_CAST")
                result as ApiResponse<ConversionRate>
            }
        }
    }
}

package io.github.tuguzT.currencyconverter.repository

import com.haroldadmin.cnradapter.NetworkResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateAPI {
    companion object {
        private const val API_KEY = "SECRET"
        const val baseURI = "https://v6.exchangerate-api.com/v6/$API_KEY/"
    }

    @GET("codes")
    suspend fun supportedCodes(): ApiResponse<SupportedCodesResult>

    @GET("pair/{base}/{target}/{amount}")
    suspend fun pairConversion(
        @Path("base") base: String,
        @Path("target") target: String,
        @Path("amount") amount: Double,
    ): ApiResponse<PairConversionResult>
}

typealias ApiResponse<T> = NetworkResponse<T, ApiError>

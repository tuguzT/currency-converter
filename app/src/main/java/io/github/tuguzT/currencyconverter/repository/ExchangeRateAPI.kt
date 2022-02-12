package io.github.tuguzT.currencyconverter.repository

import io.github.tuguzT.currencyconverter.model.repository.SupportedCodesResult
import io.github.tuguzT.currencyconverter.model.repository.PairConversionResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateAPI {
    companion object {
        private const val API_KEY = "SECRET"
        const val baseURI = "https://v6.exchangerate-api.com/v6/$API_KEY/"
    }

    @GET("/codes")
    fun supportedCodes(): Call<SupportedCodesResult>

    @GET("/pair/{base}/{target}/{amount}")
    fun pairConversion(
        @Path("base") base: String,
        @Path("target") target: String,
        @Path("amount") amount: Double,
    ): Call<PairConversionResult>
}

package io.github.tuguzT.currencyconverter.model.repository

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PairConversionResult(
    @SerialName("conversion_rate") val rate: Double,
    @SerialName("conversion_result") val result: Double,
)

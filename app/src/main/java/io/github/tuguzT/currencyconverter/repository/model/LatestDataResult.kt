package io.github.tuguzT.currencyconverter.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LatestDataResult(
    @SerialName("time_last_update_unix")
    val lastUpdateUnix: Long,

    @SerialName("time_next_update_unix")
    val nextUpdateUnix: Long,

    @SerialName("conversion_rates")
    val conversionRates: Map<String, Double>,
)

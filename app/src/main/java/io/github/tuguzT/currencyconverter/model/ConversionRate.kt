package io.github.tuguzT.currencyconverter.model

import java.util.*

data class ConversionRate(
    val baseCode: String,
    val targetCode: String,
    val lastUpdate: Date,
    val nextUpdate: Date,
    val rate: Double,
) {
    init {
        require(baseCode != targetCode) { "Base and target codes mustn't be equal" }
        require(lastUpdate < nextUpdate) { "The last update must be before the next update" }
        require(rate > 0) { "Rate must be positive, got $rate" }
    }

    val isOutdated: Boolean get() = Date() > nextUpdate
}

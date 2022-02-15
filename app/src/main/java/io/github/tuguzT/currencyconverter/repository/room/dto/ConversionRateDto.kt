package io.github.tuguzT.currencyconverter.repository.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import io.github.tuguzT.currencyconverter.repository.room.converters.DateConverter
import java.util.*

@Entity(tableName = "conversion_rate", primaryKeys = ["base_code", "target_code"])
@TypeConverters(DateConverter::class)
data class ConversionRateDto(
    @ColumnInfo(name = "base_code") val baseCode: String,
    @ColumnInfo(name = "target_code") val targetCode: String,
    @ColumnInfo(name = "last_update") val lastUpdate: Date,
    @ColumnInfo(name = "next_update") val nextUpdate: Date,
    val rate: Double,
) {
    init {
        require(baseCode != targetCode) { "Base and target codes mustn't be equal" }
        require(lastUpdate < nextUpdate) { "The last update must be before the next update" }
        require(rate > 0) { "Rate must be positive, got $rate" }
    }

    val isOutdated: Boolean get() = Date() > nextUpdate
}

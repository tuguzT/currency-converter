package io.github.tuguzT.currencyconverter.repository.room.dto

import androidx.room.Embedded
import androidx.room.Relation

data class RateWithCodes(
    @Embedded val rate: ConversionRateDto,
    @Relation(entityColumn = "code", parentColumn = "base_code")
    val baseCode: SupportedCodeDto,
    @Relation(entityColumn = "code", parentColumn = "target_code")
    val targetCode: SupportedCodeDto,
)

package io.github.tuguzT.currencyconverter.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.tuguzT.currencyconverter.repository.room.dto.ConversionRateDto

@Dao
interface ConversionRateDao : BaseDao<ConversionRateDto> {
    @Query("SELECT * FROM conversion_rate WHERE base_code = :baseCode AND target_code = :targetCode")
    suspend fun findByCodes(baseCode: String, targetCode: String): ConversionRateDto?

    @Query("SELECT * FROM conversion_rate")
    suspend fun getAll(): List<ConversionRateDto>
}

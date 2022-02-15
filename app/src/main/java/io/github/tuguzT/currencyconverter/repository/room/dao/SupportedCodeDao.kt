package io.github.tuguzT.currencyconverter.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import io.github.tuguzT.currencyconverter.repository.room.dto.SupportedCodeDto

@Dao
interface SupportedCodeDao : BaseDao<SupportedCodeDto> {
    @Query("SELECT * FROM supported_code WHERE code = :code")
    suspend fun findByCode(code: String): SupportedCodeDto?

    @Query("SELECT * FROM supported_code")
    suspend fun getAll(): List<SupportedCodeDto>
}

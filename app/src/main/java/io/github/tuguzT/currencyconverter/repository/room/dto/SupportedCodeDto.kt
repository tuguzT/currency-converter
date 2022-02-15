package io.github.tuguzT.currencyconverter.repository.room.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supported_code")
data class SupportedCodeDto(
    @PrimaryKey val code: String,
    val name: String,
)

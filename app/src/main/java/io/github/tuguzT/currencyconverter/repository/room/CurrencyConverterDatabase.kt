package io.github.tuguzT.currencyconverter.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.tuguzT.currencyconverter.repository.room.dao.ConversionRateDao
import io.github.tuguzT.currencyconverter.repository.room.dao.SupportedCodeDao
import io.github.tuguzT.currencyconverter.repository.room.dto.ConversionRateDto
import io.github.tuguzT.currencyconverter.repository.room.dto.SupportedCodeDto

@Database(
    entities = [SupportedCodeDto::class, ConversionRateDto::class],
    version = 1,
    exportSchema = false,
)
abstract class CurrencyConverterDatabase : RoomDatabase() {
    abstract val supportedCodeDao: SupportedCodeDao
    abstract val conversionRateDao: ConversionRateDao
}

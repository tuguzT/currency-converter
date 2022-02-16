package io.github.tuguzT.currencyconverter.repository.room

import io.github.tuguzT.currencyconverter.repository.Repository
import io.github.tuguzT.currencyconverter.repository.room.dto.ConversionRateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConversionRateRepository(private val database: CurrencyConverterDatabase) :
    Repository<ConversionRateDto, Pair<String, String>> {

    private val conversionRateDao get() = database.conversionRateDao

    private val context = Dispatchers.IO

    override suspend fun getAll(): List<ConversionRateDto> =
        withContext(context) { conversionRateDao.getAll() }

    override suspend fun findById(id: Pair<String, String>): ConversionRateDto? {
        val baseCode = id.first
        val targetCode = id.second
        return withContext(context) { conversionRateDao.findByCodes(baseCode, targetCode) }
    }

    override suspend fun save(item: ConversionRateDto): Unit = withContext(context) {
        when (findById(item.baseCode to item.targetCode)) {
            null -> conversionRateDao.insert(item)
            else -> conversionRateDao.update(item)
        }
    }

    override suspend fun delete(item: ConversionRateDto): Unit =
        withContext(context) { conversionRateDao.delete(item) }
}

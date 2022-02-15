package io.github.tuguzT.currencyconverter.repository.room

import io.github.tuguzT.currencyconverter.repository.Repository
import io.github.tuguzT.currencyconverter.repository.room.dto.ConversionRateDto
import io.github.tuguzT.currencyconverter.repository.room.dto.RateWithCodes
import io.github.tuguzT.currencyconverter.repository.room.dto.SupportedCodeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConversionRateRepository(private val database: CurrencyConverterDatabase) :
    Repository<ConversionRateDto, Pair<SupportedCodeDto, SupportedCodeDto>> {

    private val conversionRateDao get() = database.conversionRateDao

    private val context = Dispatchers.IO

    override suspend fun getAll(): List<ConversionRateDto> =
        withContext(context) { conversionRateDao.getAll() }

    suspend fun getAllJoined(): List<RateWithCodes> =
        withContext(context) { conversionRateDao.getAllJoined() }

    override suspend fun findById(id: Pair<SupportedCodeDto, SupportedCodeDto>): ConversionRateDto? {
        val baseCode = id.first.code
        val targetCode = id.second.code
        return withContext(context) { conversionRateDao.findByCodes(baseCode, targetCode) }
    }

    override suspend fun insert(item: ConversionRateDto) =
        withContext(context) { conversionRateDao.insert(item) }

    override suspend fun update(item: ConversionRateDto) =
        withContext(context) { conversionRateDao.update(item) }
}

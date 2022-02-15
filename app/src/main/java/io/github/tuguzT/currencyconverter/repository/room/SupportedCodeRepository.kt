package io.github.tuguzT.currencyconverter.repository.room

import io.github.tuguzT.currencyconverter.repository.Repository
import io.github.tuguzT.currencyconverter.repository.room.dto.SupportedCodeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SupportedCodeRepository(private val database: CurrencyConverterDatabase) :
    Repository<SupportedCodeDto, String> {

    private val supportedCodeDao get() = database.supportedCodeDao

    private val context = Dispatchers.IO

    override suspend fun getAll(): List<SupportedCodeDto> =
        withContext(context) { supportedCodeDao.getAll() }

    override suspend fun findById(id: String): SupportedCodeDto? =
        withContext(context) { supportedCodeDao.findByCode(id) }

    override suspend fun insert(item: SupportedCodeDto) =
        withContext(context) { supportedCodeDao.insert(item) }

    override suspend fun update(item: SupportedCodeDto) =
        withContext(context) { supportedCodeDao.update(item) }
}

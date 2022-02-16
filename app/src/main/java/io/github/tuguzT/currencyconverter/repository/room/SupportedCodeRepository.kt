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

    override suspend fun save(item: SupportedCodeDto): Unit = withContext(context) {
        when (findById(item.code)) {
            null -> supportedCodeDao.insert(item)
            else -> supportedCodeDao.update(item)
        }
    }

    override suspend fun delete(item: SupportedCodeDto): Unit =
        withContext(context) { supportedCodeDao.delete(item) }
}

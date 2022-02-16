package io.github.tuguzT.currencyconverter.repository

interface Repository<T, I> {
    suspend fun getAll(): List<T>

    suspend fun findById(id: I): T?

    suspend fun save(item: T)

    suspend fun delete(item: T)
}

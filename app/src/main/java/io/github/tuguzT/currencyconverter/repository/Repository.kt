package io.github.tuguzT.currencyconverter.repository

interface Repository<T, I> {
    suspend fun getAll(): List<T>

    suspend fun findById(id: I): T?

    suspend fun insert(item: T)

    suspend fun update(item: T)
}

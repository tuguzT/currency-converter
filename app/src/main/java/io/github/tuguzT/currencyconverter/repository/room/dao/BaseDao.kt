package io.github.tuguzT.currencyconverter.repository.room.dao

import androidx.room.Insert
import androidx.room.Update

sealed interface BaseDao<T> {
    @Insert
    suspend fun insert(item: T)

    @Update
    suspend fun update(item: T)
}

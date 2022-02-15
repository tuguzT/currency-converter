package io.github.tuguzT.currencyconverter.repository.room.converters

import androidx.room.TypeConverter
import java.util.*

object DateConverter {
    @JvmStatic
    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @JvmStatic
    @TypeConverter
    fun toDate(date: Long?): Date? = date?.let { Date(it) }
}

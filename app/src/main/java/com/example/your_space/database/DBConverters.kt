package com.example.your_space.database

import androidx.room.TypeConverter
import java.sql.Date
import java.sql.Time

object DBConverters {
    @TypeConverter
    @JvmStatic
    fun toDBDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    @JvmStatic
    fun fromDBDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    @JvmStatic
    fun toDBTime(value: Long?): Time? {
        return value?.let { Time(it) }
    }

    @TypeConverter
    @JvmStatic
    fun fromDBTime(time: Time?): Long? {
        return time?.time
    }
}
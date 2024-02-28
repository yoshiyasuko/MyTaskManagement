package com.dashimaki_dofu.mytaskmanagement.database.typeConverter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset


/**
 * LocalTimeConverter
 *
 * Created by Yoshiyasu on 2024/02/28
 */

class LocalTimeConverter {
    @TypeConverter
    fun fromTimestamp(millis: Long?) : LocalTime? {
        return millis?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalTime() }
    }

    @TypeConverter
    fun toTimestamp(time: LocalTime?) : Long? {
        return time?.atDate(LocalDate.now(ZoneId.systemDefault()))?.toInstant(ZoneOffset.ofHours(9))
            ?.toEpochMilli()
    }
}

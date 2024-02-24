package com.dashimaki_dofu.mytaskmanagement.database.typeConverter

import androidx.room.TypeConverter
import java.time.Instant


/**
 * DateConverter
 *
 * Created by Yoshiyasu on 2024/02/10
 */

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Instant?): Long? {
        return date?.toEpochMilli()
    }
}

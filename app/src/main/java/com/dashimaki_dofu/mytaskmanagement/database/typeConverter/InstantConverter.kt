package com.dashimaki_dofu.mytaskmanagement.database.typeConverter

import androidx.room.TypeConverter
import java.time.Instant


/**
 * InstantConverter
 *
 * Created by Yoshiyasu on 2024/02/10
 */

class InstantConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Instant?): Long? {
        return date?.toEpochMilli()
    }
}

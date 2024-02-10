package com.dashimaki_dofu.mytaskmanagement.database.typeConverter

import androidx.room.TypeConverter
import java.util.Date


/**
 * DateConverter
 *
 * Created by Yoshiyasu on 2024/02/10
 */

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}

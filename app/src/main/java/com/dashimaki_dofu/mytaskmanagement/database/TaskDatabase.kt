package com.dashimaki_dofu.mytaskmanagement.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dashimaki_dofu.mytaskmanagement.database.typeConverter.DateConverter
import com.dashimaki_dofu.mytaskmanagement.model.SubTask
import com.dashimaki_dofu.mytaskmanagement.model.Task


/**
 * TaskDatabase
 *
 * Created by Yoshiyasu on 2024/02/10
 */

const val roomSchemaVersion = 1
const val defaultId = 0

@Database(
    entities = [Task::class, SubTask::class],
    version = roomSchemaVersion,
    exportSchema = true
)
@TypeConverters(DateConverter::class)
abstract class TaskDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "tasks"
    }
    abstract fun taskSubjectDao(): TaskSubjectDao
}

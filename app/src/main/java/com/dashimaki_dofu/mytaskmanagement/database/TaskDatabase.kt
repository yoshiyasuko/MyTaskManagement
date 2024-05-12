package com.dashimaki_dofu.mytaskmanagement.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.dashimaki_dofu.mytaskmanagement.database.typeConverter.InstantConverter
import com.dashimaki_dofu.mytaskmanagement.database.typeConverter.LocalTimeConverter
import com.dashimaki_dofu.mytaskmanagement.model.SubTask
import com.dashimaki_dofu.mytaskmanagement.model.Task


/**
 * TaskDatabase
 *
 * Created by Yoshiyasu on 2024/02/10
 */

const val roomSchemaVersion = 3
const val defaultId = 0

@Database(
    entities = [Task::class, SubTask::class],
    version = roomSchemaVersion,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, TaskNameDeadlineToDeadlineDateMigrationSpec::class),
        AutoMigration(from = 2, to = 3, TaskAddMemoMigrationSpec::class)
    ]
)
@TypeConverters(InstantConverter::class, LocalTimeConverter::class)
abstract class TaskDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "tasks"
    }
    abstract fun taskSubjectDao(): TaskSubjectDao
}

@RenameColumn(
    tableName = "tasks",
    fromColumnName = "deadline",
    toColumnName = "deadlineDate"
)
class TaskNameDeadlineToDeadlineDateMigrationSpec : AutoMigrationSpec
class TaskAddMemoMigrationSpec : AutoMigrationSpec

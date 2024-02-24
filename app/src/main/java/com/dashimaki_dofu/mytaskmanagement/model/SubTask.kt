package com.dashimaki_dofu.mytaskmanagement.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.dashimaki_dofu.mytaskmanagement.R
import com.dashimaki_dofu.mytaskmanagement.database.defaultId


/**
 * SubTask
 *
 * Created by Yoshiyasu on 2024/02/11
 */

// 子課題
@Entity(
    tableName = "subTasks",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("taskId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SubTask(
    @PrimaryKey(autoGenerate = true) var id: Int = defaultId,
    var taskId: Int = defaultId,
    var title: String = "",
    var status: SubTaskStatus = SubTaskStatus.INCOMPLETE
) {
    val stampResourceId: Int?
        get() {
            return when (status) {
                SubTaskStatus.INCOMPLETE -> R.drawable.mark_incomplete_checked
                SubTaskStatus.COMPLETED -> R.drawable.mark_sumi_checked
                else -> null
            }
        }
}

enum class SubTaskStatus {
    INCOMPLETE, // 未着手
    ACTIVE,     // 着手中
    COMPLETED,  // 完了
}

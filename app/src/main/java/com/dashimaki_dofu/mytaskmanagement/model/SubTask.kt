package com.dashimaki_dofu.mytaskmanagement.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
    @ColumnInfo(index = true) var taskId: Int = defaultId,
    var title: String = "",
    var status: SubTaskStatus = SubTaskStatus.INCOMPLETE
)

enum class SubTaskStatus(
    @DrawableRes val stampResourceId: Int?,
    @StringRes val stringResourceId: Int
) {
    INCOMPLETE(R.drawable.mark_incomplete_checked, R.string.subTask_status_incomplete), // 未着手
    ACTIVE(null, R.string.subTask_status_active),     // 着手中
    COMPLETED(R.drawable.mark_sumi_checked, R.string.subTask_status_completed);  // 完了
}

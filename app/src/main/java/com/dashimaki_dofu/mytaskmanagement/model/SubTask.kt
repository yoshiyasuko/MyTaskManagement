package com.dashimaki_dofu.mytaskmanagement.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.dashimaki_dofu.mytaskmanagement.R


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
    @PrimaryKey(autoGenerate = true) var id: Int = -1,
    @ColumnInfo(index = true) var taskId: Int = -1,
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

// ダミーの子課題リスト: デバッグ用
fun makeDummySubTasks(taskId: Int = 0): List<SubTask> {
    return (0..<10).map {
        SubTask(
            id = it,
            taskId = taskId,
            title = "課題${taskId + 1}の子課題${it + 1}",
            status = when (taskId % 3) {
                0 -> when (it) {
                    in 0..1 -> SubTaskStatus.COMPLETED
                    2 -> SubTaskStatus.ACTIVE
                    else -> SubTaskStatus.INCOMPLETE
                }
                1 -> when (it) {
                    in 0..2 -> SubTaskStatus.COMPLETED
                    3 -> SubTaskStatus.ACTIVE
                    else -> SubTaskStatus.INCOMPLETE
                }
                else -> when (it) {
                    in 0..4 -> SubTaskStatus.COMPLETED
                    5 -> SubTaskStatus.ACTIVE
                    else -> SubTaskStatus.INCOMPLETE
                }
            }
        )
    }
}

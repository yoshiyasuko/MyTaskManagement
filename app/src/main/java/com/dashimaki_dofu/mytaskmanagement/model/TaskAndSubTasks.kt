package com.dashimaki_dofu.mytaskmanagement.model

import androidx.room.Embedded
import androidx.room.Relation


/**
 * TaskAndSubTasks
 *
 * Created by Yoshiyasu on 2024/02/11
 */

// 課題 : 子課題 = 1 : N
class TaskAndSubTasks {
    @Embedded
    lateinit var task: Task

    @Relation(parentColumn = "id", entityColumn = "taskId")
    lateinit var subTasks: List<SubTask>

    // 課題の進捗率: 完了した課題の割合
    val progressRate: Float
        get() {
            if (subTasks.isEmpty()) return 0f
            return subTasks.count { it.status == SubTaskStatus.COMPLETED } / subTasks.count().toFloat()
        }
}

// 「課題と子課題ペア」のダミーデータ
fun makeDummyAllTaskAndSubTasks(): List<TaskAndSubTasks> {
    return (0..<20).map {
        val taskAndSubTasks = TaskAndSubTasks()
        taskAndSubTasks.task = makeDummyTask(it)
        taskAndSubTasks.subTasks = makeDummySubTasks(taskId = it)
        taskAndSubTasks
    }
}

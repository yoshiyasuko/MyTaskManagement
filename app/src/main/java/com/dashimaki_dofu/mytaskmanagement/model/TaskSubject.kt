package com.dashimaki_dofu.mytaskmanagement.model

import androidx.room.Embedded
import androidx.room.Relation


/**
 * TaskSubject
 *
 * Created by Yoshiyasu on 2024/02/11
 */

// 「課題 : 子課題 = 1 : N」のリレーションを実現するラッパーオブジェクト
class TaskSubject {
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

package com.dashimaki_dofu.mytaskmanagement.model

import androidx.compose.ui.graphics.Color
import androidx.room.Embedded
import androidx.room.Relation
import com.dashimaki_dofu.mytaskmanagement.ui.theme.TaskColor


/**
 * TaskSubject
 *
 * Created by Yoshiyasu on 2024/02/11
 */

// 「課題 : 子課題 = 1 : N」のリレーションを実現するラッパーオブジェクト
class TaskSubject {
    companion object {
        fun initialize(): TaskSubject {
            return TaskSubject().also {
                it.task = Task()
                it.subTasks = mutableListOf()
            }
        }
    }

    @Embedded
    lateinit var task: Task

    @Relation(parentColumn = "id", entityColumn = "taskId")
    lateinit var subTasks: MutableList<SubTask>

    // 課題の進捗率: 完了した課題の割合
    val progressRate: Float
        get() {
            if (subTasks.isEmpty()) return 0f
            return subTasks.count { it.status == SubTaskStatus.COMPLETED } / subTasks
                .count()
                .toFloat()
        }

    // 課題の進捗率表示用
    val progressRateString: String
        get() {
            return "${(progressRate * 100).toInt()}%"
        }

    // 課題の進捗率表示のフォントカラー
    //  100%の場合だけ緑色、それ以外は黒
    val progressRateStringColor: Color
        get() {
            return if (subTasks.any { it.status != SubTaskStatus.COMPLETED } || subTasks.isEmpty()) {
                TaskColor.LIGHT_GRAY.color
            } else {
                TaskColor.LIGHT_GREEN.color
            }
        }
}

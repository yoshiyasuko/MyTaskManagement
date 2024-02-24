package com.dashimaki_dofu.mytaskmanagement.model

import com.dashimaki_dofu.mytaskmanagement.ui.theme.TaskColor
import java.time.Instant


/**
 * DummyModels
 *
 * Created by Yoshiyasu on 2024/02/12
 */

// ダミーの課題ラッパー: デバッグ用
fun makeDummyTaskSubjects(): List<TaskSubject> {
    return (0..<20).map {
        val taskSubject = TaskSubject()
        taskSubject.task = makeDummyTask(it)
        taskSubject.subTasks = makeDummySubTasks(taskId = it)
        taskSubject
    }
}

// ダミーの課題: デバッグ用
fun makeDummyTask(taskId: Int): Task {
    return Task(
        id = taskId,
        title = "課題${taskId + 1}",
        colorValue = when (taskId % 3) {
            0 -> TaskColor.YELLOW.code
            1 -> TaskColor.RED.code
            else -> TaskColor.BLUE.code
        },
        deadline = Instant.now()
    )
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

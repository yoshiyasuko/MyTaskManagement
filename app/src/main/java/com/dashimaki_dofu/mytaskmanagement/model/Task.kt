package com.dashimaki_dofu.mytaskmanagement.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.graphics.toColor
import java.text.SimpleDateFormat
import java.util.Date

// 課題
data class Task(
    var id: Int,
    var title: String,
    var color: Color,
    var subTasks: List<SubTask> = emptyList(),
    var deadline: Date? = null
) {
    // 締切表示: "M/d"形式
    val formattedDeadLineString: String
        get() {
            val formatter = SimpleDateFormat("M/d")
            deadline?.let {
                return formatter.format(it)
            }
            return "-"
        }

    // 課題リスト上タイトルのフォントカラー
    val listTitleColor: Color
        get() {
            // 明るさを暗くする
            val argb = color.toArgb()
            val hsl = FloatArray(3) { 0f }
            ColorUtils.RGBToHSL(
                argb.red,
                argb.green,
                argb.blue,
                hsl
            )
            hsl[2] -= 0.3f
            return Color(ColorUtils.HSLToColor(hsl).toColor().toArgb())
        }

    // 課題の進捗率: 完了した課題の割合
    val progressRate: Float
        get() {
            if (subTasks.isEmpty()) return 0f
            return subTasks.count { it.status == SubTaskStatus.COMPLETED } / subTasks.count().toFloat()
        }
}

// 子課題
data class SubTask(
    var id: Int,
    var title: String = "",
    var status: SubTaskStatus = SubTaskStatus.INCOMPLETE
)

enum class SubTaskStatus {
    INCOMPLETE, // 未着手
    ACTIVE,     // 着手中
    COMPLETED,  // 完了
}

// ダミーの課題リスト: デバッグ用
fun makeDummyTasks(numberOfTask: Int = 20): List<Task> {
    return (0..<numberOfTask).map {
        Task(
            id = it,
            title = "課題${it + 1}",
            color = when (it % 3) {
                0 -> Color(0xfff8dc6c)
                1 -> Color(0xfff86e6c)
                else -> Color(0xff6cbef8)
            },
            subTasks = makeDummySubTasks(it),
            deadline = Date()
        )
    }
}

// ダミーの子課題リスト: デバッグ用
fun makeDummySubTasks(taskId: Int = 0): List<SubTask> {
    return (0..<10).map {
        SubTask(
            id = it,
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

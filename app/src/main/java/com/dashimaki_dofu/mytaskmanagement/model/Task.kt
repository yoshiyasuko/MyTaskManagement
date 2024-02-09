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
    var title: String = "",
    var status: SubTaskStatus = SubTaskStatus.INCOMPLETE
)

enum class SubTaskStatus {
    INCOMPLETE, // 未着手
    ACTIVE,     // 着手中
    COMPLETED,  // 完了
}

fun makeDummyTasks(): List<Task> {
    return listOf(
        Task(
            id = 0,
            title = "課題1",
            color = Color(0xfff8dc6c),
            subTasks = listOf(
                SubTask(
                    title = "課題1の子課題1",
                    status = SubTaskStatus.INCOMPLETE
                ),
                SubTask(
                    title = "課題1の子課題2",
                    status = SubTaskStatus.INCOMPLETE
                ),
                SubTask(
                    title = "課題1の子課題3",
                    status = SubTaskStatus.COMPLETED
                ),
            ),
            deadline = Date()
        ),
        Task(
            id = 1,
            title = "課題2",
            color = Color(0xfff86e6c),
            subTasks = listOf(
                SubTask(
                    title = "課題2の子課題1",
                    status = SubTaskStatus.INCOMPLETE
                ),
                SubTask(
                    title = "課題2の子課題2",
                    status = SubTaskStatus.INCOMPLETE
                ),
                SubTask(
                    title = "課題2の子課題3",
                    status = SubTaskStatus.COMPLETED
                ),
                SubTask(
                    title = "課題2の子課題4",
                    status = SubTaskStatus.COMPLETED
                ),
            ),
            deadline = Date()
        ),
        Task(
            id = 2,
            title = "課題3",
            color = Color(0xff6cbef8),
            subTasks = listOf(
                SubTask(
                    title = "課題3の子課題1",
                    status = SubTaskStatus.INCOMPLETE
                ),
                SubTask(
                    title = "課題3の子課題2",
                    status = SubTaskStatus.COMPLETED
                ),
                SubTask(
                    title = "課題3の子課題3",
                    status = SubTaskStatus.COMPLETED
                ),
                SubTask(
                    title = "課題3の子課題4",
                    status = SubTaskStatus.COMPLETED
                ),
            ),
            deadline = Date()
        )
    )
}

// ダミーの小課題リスト: デバッグ用
fun makeDummySubTasks(): List<SubTask> {
    return (0..<5).map {
        SubTask(
            title = "サブタスク$it",
            status = if (it % 2 == 1) SubTaskStatus.COMPLETED else SubTaskStatus.INCOMPLETE)
    }
}

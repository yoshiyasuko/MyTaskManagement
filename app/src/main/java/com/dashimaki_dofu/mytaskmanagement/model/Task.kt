package com.dashimaki_dofu.mytaskmanagement.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.graphics.toColor
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dashimaki_dofu.mytaskmanagement.database.defaultId
import com.dashimaki_dofu.mytaskmanagement.ui.theme.TaskColor
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// 課題
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Int = defaultId,
    var title: String = "",
    var colorValue: Long = TaskColor.YELLOW.code,
    var deadlineDate: Instant? = null,
    var deadlineTime: LocalTime? = null
) {
    // 締切を省略表示: "M/d"形式
    val formattedDeadLineString: String
        get() {
            return deadlineDate?.let {
                val localDateTime = LocalDateTime.ofInstant(deadlineDate, ZoneId.systemDefault())
                val formatter = DateTimeFormatter.ofPattern("M/d")
                formatter.format(localDateTime)
            } ?: ""
        }

    // 締切を詳細表示: "M/d HH:mm"形式
    val formattedDeadLineDetailString: String
        get() {
            val deadlineTimeString = deadlineTime?.let {
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                " ${it.format(formatter)}"
            } ?: ""
            return formattedDeadLineString + deadlineTimeString
        }

    // 課題のベースカラー
    val color: Color
        get() {
            return Color(colorValue)
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
}

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
import java.text.SimpleDateFormat
import java.util.Date
import com.dashimaki_dofu.mytaskmanagement.database.defaultId
import com.dashimaki_dofu.mytaskmanagement.ui.theme.TaskColor
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// 課題
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Int = defaultId,
    var title: String = "",
    var deadline: Date? = null
    var colorValue: Long = TaskColor.YELLOW.code,
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

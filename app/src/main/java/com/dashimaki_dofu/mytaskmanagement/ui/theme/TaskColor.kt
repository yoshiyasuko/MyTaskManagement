package com.dashimaki_dofu.mytaskmanagement.ui.theme

import androidx.compose.ui.graphics.Color


/**
 * TaskColor
 *
 * Created by Yoshiyasu on 2024/02/23
 */

enum class TaskColor(val code: Long) {
    YELLOW(0xfff8dc6c),
    RED(0xfff86e6c),
    BLUE(0xff6cbef8),
    LIGHT_GREEN(0xff149a48);

    companion object {
        fun of(code: Long): TaskColor {
            TaskColor.entries.forEach {
                if (it.code == code) return it
            }
            return YELLOW
        }
    }

    val color: Color = Color(code)
}

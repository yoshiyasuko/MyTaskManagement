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
    LIGHT_GREEN(0xff149a48),
    LIGHT_GRAY(0xff1c1d1d);

    val color: Color = Color(code)
}

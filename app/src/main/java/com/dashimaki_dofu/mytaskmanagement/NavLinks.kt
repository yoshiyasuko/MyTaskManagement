package com.dashimaki_dofu.mytaskmanagement


/**
 * NavLinks
 *
 * Created by Yoshiyasu on 2024/02/10
 */

/// 画面遷移情報
sealed class NavLinks(val route: String) {
    data object TaskList : NavLinks("taskList")
    data object TaskDetail : NavLinks("taskDetail/{id}") {
        const val ARGUMENT_ID = "id"
        fun createRoute(id: Int): String {
            return "taskDetail/${id}"
        }
    }

    data object TaskCreate : NavLinks("taskCreate")
    data object TaskEdit : NavLinks("taskEdit/{id}") {
        const val ARGUMENT_ID = "id"
        fun createRoute(id: Int): String {
            return "taskEdit/${id}"
        }
    }
}

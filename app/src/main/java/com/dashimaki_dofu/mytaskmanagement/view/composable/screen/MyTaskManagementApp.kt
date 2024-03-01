package com.dashimaki_dofu.mytaskmanagement.view.composable.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dashimaki_dofu.mytaskmanagement.NavLinks
import com.dashimaki_dofu.mytaskmanagement.ui.theme.MyTaskManagementTheme
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskDetailViewModelImpl
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskEditViewModelImpl
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskListViewModelImpl


/**
 * MainScreen
 *
 * Created by Yoshiyasu on 2024/02/13
 */

@Composable
fun MyTaskManagementApp(
    onClickSendFeedback: () -> Unit
) {
    MyTaskManagementTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = NavLinks.TaskList.route,
        ) {
            // 課題一覧画面
            composable(
                route = NavLinks.TaskList.route
            ) {
                val viewModel = hiltViewModel<TaskListViewModelImpl>()
                TaskListScreen(
                    viewModel = viewModel,
                    onClickItem = { taskId ->
                        navController.navigate(NavLinks.TaskDetail.createRoute(taskId))
                    },
                    onClickAddTaskButton = {
                        navController.navigate(NavLinks.TaskCreate.route)
                    },
                    onClickSendFeedback = onClickSendFeedback
                )
            }

            // 課題詳細画面
            composable(
                route = NavLinks.TaskDetail.route,
                arguments = listOf(
                    navArgument(NavLinks.TaskDetail.ARGUMENT_ID) {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val viewModel = hiltViewModel<TaskDetailViewModelImpl>()
                val taskId = backStackEntry.arguments?.getInt(NavLinks.TaskDetail.ARGUMENT_ID) ?: -1
                TaskDetailScreen(
                    taskId = taskId,
                    viewModel = viewModel,
                    onClickNavigationIcon = {
                        navController.navigateUp()
                    },
                    onClickEditButton = {
                        navController.navigate(NavLinks.TaskEdit.createRoute(taskId))
                    },
                    onDeleteCompleted = {
                        navController.navigateUp()
                    }
                )
            }

            // 課題追加画面
            composable(
                route = NavLinks.TaskCreate.route
            ) {
                val viewModel = hiltViewModel<TaskEditViewModelImpl>()
                TaskEditScreen(
                    viewModel = viewModel,
                    onClickNavigationIcon = {
                        navController.navigateUp()
                    },
                    onSaveCompleted = {
                        navController.navigateUp()
                    }
                )
            }

            // 課題編集画面
            composable(
                route = NavLinks.TaskEdit.route,
                arguments = listOf(
                    navArgument(NavLinks.TaskEdit.ARGUMENT_ID) {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val viewModel = hiltViewModel<TaskEditViewModelImpl>()
                val taskId = backStackEntry.arguments?.getInt(NavLinks.TaskEdit.ARGUMENT_ID)
                TaskEditScreen(
                    taskId = taskId,
                    viewModel = viewModel,
                    onClickNavigationIcon = {
                        navController.navigateUp()
                    },
                    onSaveCompleted = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

package com.dashimaki_dofu.mytaskmanagement.view.composable.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dashimaki_dofu.mytaskmanagement.NavLinks.Calendar
import com.dashimaki_dofu.mytaskmanagement.NavLinks.TaskCreate
import com.dashimaki_dofu.mytaskmanagement.NavLinks.TaskDetail
import com.dashimaki_dofu.mytaskmanagement.NavLinks.TaskEdit
import com.dashimaki_dofu.mytaskmanagement.NavLinks.TaskList
import com.dashimaki_dofu.mytaskmanagement.ui.theme.MyTaskManagementTheme
import com.dashimaki_dofu.mytaskmanagement.view.composable.MyBottomNavigation
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
        Scaffold(
            bottomBar = {
                MyBottomNavigation(navController = navController)
            }
        ) {
            //region NavHost
            NavHost(
                modifier = Modifier.padding(it),
                navController = navController,
                startDestination = TaskList.route,
            ) {
                //region 課題一覧画面
                composable(
                    route = TaskList.route
                ) {
                    val viewModel = hiltViewModel<TaskListViewModelImpl>()
                    TaskListScreen(
                        viewModel = viewModel,
                        onClickItem = { taskId ->
                            navController.navigate(TaskDetail.createRoute(taskId))
                        },
                        onClickAddTaskButton = {
                            navController.navigate(TaskCreate.route)
                        },
                        onClickSendFeedback = onClickSendFeedback
                    )
                }
                //endregion

                //region 課題詳細画面
                composable(
                    route = TaskDetail.route,
                    arguments = listOf(
                        navArgument(TaskDetail.ARGUMENT_ID) {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->
                    val viewModel = hiltViewModel<TaskDetailViewModelImpl>()
                    val taskId = backStackEntry.arguments?.getInt(TaskDetail.ARGUMENT_ID) ?: -1
                    TaskDetailScreen(
                        taskId = taskId,
                        viewModel = viewModel,
                        onClickNavigationIcon = {
                            navController.navigateUp()
                        },
                        onClickEditButton = {
                            navController.navigate(TaskEdit.createRoute(taskId))
                        },
                        onDeleteCompleted = {
                            navController.navigateUp()
                        }
                    )
                }
                //endregion

                //region 課題追加画面
                composable(
                    route = TaskCreate.route
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
                //endregion

                //region 課題編集画面
                composable(
                    route = TaskEdit.route,
                    arguments = listOf(
                        navArgument(TaskEdit.ARGUMENT_ID) {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->
                    val viewModel = hiltViewModel<TaskEditViewModelImpl>()
                    val taskId = backStackEntry.arguments?.getInt(TaskEdit.ARGUMENT_ID)
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
                //endregion

                //region カレンダー画面
                composable(
                    route = Calendar.route
                ) {
                    CalendarScreen()
                }
            }
            //endregion
        }
    }
}

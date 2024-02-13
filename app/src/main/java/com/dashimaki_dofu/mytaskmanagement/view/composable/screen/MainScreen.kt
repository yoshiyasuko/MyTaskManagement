package com.dashimaki_dofu.mytaskmanagement.view.composable.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dashimaki_dofu.mytaskmanagement.NavLinks
import com.dashimaki_dofu.mytaskmanagement.ui.theme.MyTaskManagementTheme
import com.dashimaki_dofu.mytaskmanagement.viewModel.MainViewModel


/**
 * MainScreen
 *
 * Created by Yoshiyasu on 2024/02/13
 */

@Composable
fun MainScreen(mainViewModel: MainViewModel = viewModel()) {
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
                TaskListScreen(
                    taskSubjects = mainViewModel.taskSubjects,
                    onClickItem = { taskId ->
                        navController.navigate(NavLinks.TaskDetail.createRoute(taskId))
                    }
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
                val taskId = backStackEntry.arguments?.getInt(NavLinks.TaskDetail.ARGUMENT_ID) ?: -1
                val task = mainViewModel.taskSubjects.first {
                    it.task.id == taskId
                }
                TaskDetailScreen(taskSubject = task, onClickNavigationIcon = {
                    navController.navigateUp()
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}

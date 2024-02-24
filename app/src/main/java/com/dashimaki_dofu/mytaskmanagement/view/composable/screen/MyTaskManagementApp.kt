package com.dashimaki_dofu.mytaskmanagement.view.composable.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dashimaki_dofu.mytaskmanagement.NavLinks
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyTaskSubjects
import com.dashimaki_dofu.mytaskmanagement.ui.theme.MyTaskManagementTheme
import com.dashimaki_dofu.mytaskmanagement.viewModel.MainViewModel
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskDetailViewModel
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskListViewModel


/**
 * MainScreen
 *
 * Created by Yoshiyasu on 2024/02/13
 */

@Composable
fun MyTaskManagementApp(mainViewModel: MainViewModel = viewModel()) {
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
                val viewModel = hiltViewModel<TaskListViewModel>()
                TaskListScreen(
                    taskListViewModel = viewModel,
                    onClickItem = { taskId ->
                        navController.navigate(NavLinks.TaskDetail.createRoute(taskId))
                    },
                    onClickAddTaskButton = {
                        navController.navigate(NavLinks.TaskCreate.route)
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyTaskManagementAppPreview() {
    val mainViewModel = MainViewModel()
    mainViewModel.taskSubjects = makeDummyTaskSubjects()
    MyTaskManagementApp(mainViewModel)
}

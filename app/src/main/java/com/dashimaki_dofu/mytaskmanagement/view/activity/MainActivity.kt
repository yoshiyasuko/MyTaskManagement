package com.dashimaki_dofu.mytaskmanagement.view.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dashimaki_dofu.mytaskmanagement.NavLinks
import com.dashimaki_dofu.mytaskmanagement.ui.theme.MyTaskManagementTheme
import com.dashimaki_dofu.mytaskmanagement.view.composable.screen.TaskDetailScreen
import com.dashimaki_dofu.mytaskmanagement.view.composable.screen.TaskListScreen
import com.dashimaki_dofu.mytaskmanagement.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.loading.value
            }
        }

        setContent {
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
                        TaskListScreen(tasks = viewModel.tasks, onClickItem = { taskId ->
                            navController.navigate(NavLinks.TaskDetail.createRoute(taskId))
                        })
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
                        val task = viewModel.tasks.first {
                            it.id == taskId
                        }
                        TaskDetailScreen(task = task, onClickNavigationIcon = {
                            navController.navigateUp()
                        })
                    }
                }
            }
        }
    }
}

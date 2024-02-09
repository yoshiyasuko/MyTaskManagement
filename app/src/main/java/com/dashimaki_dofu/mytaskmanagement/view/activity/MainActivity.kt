package com.dashimaki_dofu.mytaskmanagement.view.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dashimaki_dofu.mytaskmanagement.ui.theme.MyTaskManagementTheme
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
                TaskListScreen(tasks = viewModel.tasks)
            }
        }
    }
}

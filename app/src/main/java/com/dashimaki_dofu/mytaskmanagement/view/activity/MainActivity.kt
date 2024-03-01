package com.dashimaki_dofu.mytaskmanagement.view.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dashimaki_dofu.mytaskmanagement.R
import com.dashimaki_dofu.mytaskmanagement.view.composable.screen.MyTaskManagementApp
import com.dashimaki_dofu.mytaskmanagement.viewModel.MainViewModel
import com.google.firebase.appdistribution.ktx.appDistribution
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.loading.value
            }
        }

        super.onCreate(savedInstanceState)

        setContent {
            MyTaskManagementApp(
                onClickSendFeedback = {
                    Firebase.appDistribution.startFeedback(
                        R.string.common_feedback_screenMessage,
                        null
                    )
                }
            )
        }
    }
}

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
                    // FIXME: フィードバック送信機能。
                    //  ここのデフォルトスクリーンショットはoffにしたい。
                    //  第2引数のUriはスクリーンショットBitmapへのリンクを示していて、
                    //  メソッドのドキュメントには「ここをnullにすればデフォルトスクリーンショットをoffにできるよ」
                    //  と書いてあるが、nullにするとUri.getScheme()でNullPointerExceptionでクラッシュしてしまい機能しない。
                    //  現状これを抑制する方法が思い付かないため、第2引数に何も渡さずデフォルトスクリーンショットをonにしている。
                    Firebase
                        .appDistribution
                        .startFeedback(R.string.common_feedback_screenMessage)
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        // アプリが起動 or バックグラウンドから復帰する度にアップデートチェックを行う
        viewModel.updateIfNewReleaseAvailable()
    }
}

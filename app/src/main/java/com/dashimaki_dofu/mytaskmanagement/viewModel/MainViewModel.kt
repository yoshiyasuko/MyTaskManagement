package com.dashimaki_dofu.mytaskmanagement.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashimaki_dofu.mytaskmanagement.model.TaskAndSubTasks
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyAllTaskAndSubTasks
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/**
 * MainViewModel
 *
 * Created by Yoshiyasu on 2024/02/05
 */

class MainViewModel : ViewModel() {
    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    val taskAndSubTasks: List<TaskAndSubTasks> by lazy { makeDummyAllTaskAndSubTasks() }

    init {
        viewModelScope.launch {
            // run background task here
            delay(2000)
            _loading.value = false
        }
    }
}

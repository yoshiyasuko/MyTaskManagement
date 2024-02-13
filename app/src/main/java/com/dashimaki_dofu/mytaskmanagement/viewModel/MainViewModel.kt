package com.dashimaki_dofu.mytaskmanagement.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyTaskSubjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * MainViewModel
 *
 * Created by Yoshiyasu on 2024/02/05
 */

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    lateinit var taskSubjects: List<TaskSubject>

    init {
        viewModelScope.launch {
            // TODO: load taskSubjects from Room
            taskSubjects = makeDummyTaskSubjects()
            _loading.value = false
        }
    }
}

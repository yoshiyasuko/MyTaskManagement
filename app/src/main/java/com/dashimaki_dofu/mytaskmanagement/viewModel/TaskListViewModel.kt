package com.dashimaki_dofu.mytaskmanagement.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * TaskListViewModel
 *
 * Created by Yoshiyasu on 2024/02/13
 */

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskSubjectRepository: TaskSubjectRepository,
) : ViewModel() {
    private val _taskSubjects = MutableStateFlow<List<TaskSubject>>(emptyList())
    val taskSubjects = _taskSubjects.asStateFlow()

    fun fetchTaskSubjects() {
        viewModelScope.launch {
            _taskSubjects.value = taskSubjectRepository.getAllTaskSubjects()
        }
    }
}

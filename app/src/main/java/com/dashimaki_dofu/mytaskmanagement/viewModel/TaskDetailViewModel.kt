package com.dashimaki_dofu.mytaskmanagement.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyTaskSubjects
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * TaskDetailViewModel
 *
 * Created by Yoshiyasu on 2024/02/13
 */

abstract class TaskDetailViewModel : ViewModel() {
    abstract val taskSubject: StateFlow<TaskSubject>
    open val showDeleteAlertDialogState: StateFlow<Boolean> = MutableStateFlow(false)

    open fun fetchTaskSubject(taskId: Int) = Unit
    open fun showDeleteAlertDialog() = Unit
    open fun dismissDeleteAlertDialog() = Unit
    open fun deleteTask(taskId: Int, completion: () -> Unit) = Unit
}

@HiltViewModel
class TaskDetailViewModelImpl @Inject constructor(
    private val taskSubjectRepository: TaskSubjectRepository,
) : TaskDetailViewModel() {
    private val _taskSubject = MutableStateFlow(TaskSubject.initialize())
    override val taskSubject = _taskSubject.asStateFlow()

    private val _showDeleteAlertDialogState = MutableStateFlow(false)
    override val showDeleteAlertDialogState = _showDeleteAlertDialogState.asStateFlow()

    override fun deleteTask(taskId: Int, completion: () -> Unit) {
        viewModelScope.launch {
            taskSubjectRepository.deleteTask(taskId)
            completion.invoke()
        }
    }

    override fun fetchTaskSubject(taskId: Int) {
        viewModelScope.launch {
            _taskSubject.value = taskSubjectRepository.getTaskSubject(taskId)
        }
    }

    override fun showDeleteAlertDialog() {
        _showDeleteAlertDialogState.value = true
    }

    override fun dismissDeleteAlertDialog() {
        _showDeleteAlertDialogState.value = false
    }
}

class TaskDetailViewModelMock : TaskDetailViewModel() {
    override val taskSubject: StateFlow<TaskSubject>
        get() = MutableStateFlow(makeDummyTaskSubjects().first().also { it.subTasks = emptyList() })
}


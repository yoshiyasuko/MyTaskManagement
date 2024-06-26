package com.dashimaki_dofu.mytaskmanagement.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashimaki_dofu.mytaskmanagement.model.SubTask
import com.dashimaki_dofu.mytaskmanagement.model.SubTaskStatus
import com.dashimaki_dofu.mytaskmanagement.model.SubTaskStatus.INCOMPLETE
import com.dashimaki_dofu.mytaskmanagement.model.Task
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyTaskSubjects
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepository
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskDetailViewModel.UiState.Loaded
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskDetailViewModel.UiState.Loading
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

//region TaskDetailViewModel(abstract class)
abstract class TaskDetailViewModel : ViewModel() {
    sealed interface UiState {
        data object Loading : UiState
        data class Loaded(
            val taskSubject: TaskSubject
        ) : UiState
    }

    abstract val uiState: StateFlow<UiState>
    open val showDeleteAlertDialogState: StateFlow<Boolean> = MutableStateFlow(false)
    open val showCopyAlertDialogState: StateFlow<Boolean> = MutableStateFlow(false)

    open fun fetchTaskSubject(taskId: Int) = Unit
    open fun showDeleteAlertDialog() = Unit
    open fun dismissDeleteAlertDialog() = Unit
    open fun showCopyAlertDialog() = Unit
    open fun dismissCopyAlertDialog() = Unit
    open fun deleteTask(taskId: Int, completion: () -> Unit) = Unit
    open fun copyTask(taskSubject: TaskSubject, completion: () -> Unit) = Unit
    open fun updateSubTaskStatus(subTaskId: Int, status: SubTaskStatus) = Unit
}
//endregion

//region Impl
@HiltViewModel
class TaskDetailViewModelImpl @Inject constructor(
    private val taskSubjectRepository: TaskSubjectRepository,
) : TaskDetailViewModel() {
    private val _uiState = MutableStateFlow<UiState>(Loading)
    override val uiState = _uiState.asStateFlow()

    private val _showDeleteAlertDialogState = MutableStateFlow(false)
    override val showDeleteAlertDialogState = _showDeleteAlertDialogState.asStateFlow()

    private val _showCopyAlertDialogState = MutableStateFlow(false)
    override val showCopyAlertDialogState = _showCopyAlertDialogState.asStateFlow()

    override fun deleteTask(taskId: Int, completion: () -> Unit) {
        viewModelScope.launch {
            taskSubjectRepository.deleteTask(taskId)
            completion.invoke()
        }
    }

    override fun copyTask(taskSubject: TaskSubject, completion: () -> Unit) {
        viewModelScope.launch {
            val task = Task()
            task.title = taskSubject.task.title
            task.deadlineDate = taskSubject.task.deadlineDate
            task.deadlineTime = taskSubject.task.deadlineTime

            val taskId = taskSubjectRepository.saveTask(task)

            taskSubject.subTasks.forEach {
                val subTask = SubTask()
                subTask.taskId = taskId
                subTask.title = it.title
                subTask.status = INCOMPLETE
                taskSubjectRepository.saveSubTask(subTask)
            }
            completion.invoke()
        }
    }

    override fun fetchTaskSubject(taskId: Int) {
        viewModelScope.launch {
            val taskSubject = taskSubjectRepository.getTaskSubject(taskId)
            _uiState.value = Loaded(taskSubject)
        }
    }

    override fun showDeleteAlertDialog() {
        _showDeleteAlertDialogState.value = true
    }

    override fun dismissDeleteAlertDialog() {
        _showDeleteAlertDialogState.value = false
    }

    override fun showCopyAlertDialog() {
        _showCopyAlertDialogState.value = true
    }

    override fun dismissCopyAlertDialog() {
        _showCopyAlertDialogState.value = false
    }

    override fun updateSubTaskStatus(subTaskId: Int, status: SubTaskStatus) {
        viewModelScope.launch {
            when (val state = _uiState.value) {
                is Loaded -> {
                    val targetSubTask =
                        state.taskSubject.subTasks.firstOrNull { it.id == subTaskId }
                    targetSubTask?.let { subTask ->
                        subTask.status = status
                        taskSubjectRepository.updateSubTask(subTask)
                        val targetSubTaskIndex =
                            state.taskSubject.subTasks.indexOfFirst { it.id == subTaskId }
                        state.taskSubject.subTasks[targetSubTaskIndex] = subTask

                        // FIXME: もっと良い方法があるのでは...
                        //  TaskSubjectをdata classで実装すると、この処理でrecomposeされない
                        //  data classの弊害なのか...一旦動くこれで実装しておく
                        _uiState.value = state.copy(
                            taskSubject = TaskSubject().also {
                                it.task = state.taskSubject.task
                                it.subTasks = state.taskSubject.subTasks
                            }
                        )
                    }
                }

                else -> return@launch
            }
        }
    }
}
//endregion

//region Mock
class TaskDetailViewModelMock : TaskDetailViewModel() {
    override val uiState: StateFlow<UiState>
        get() = MutableStateFlow(
            Loaded(
                makeDummyTaskSubjects()
                    .first()
            )
        )
}
//endregion


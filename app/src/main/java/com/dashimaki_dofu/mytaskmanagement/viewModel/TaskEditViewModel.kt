package com.dashimaki_dofu.mytaskmanagement.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashimaki_dofu.mytaskmanagement.database.defaultId
import com.dashimaki_dofu.mytaskmanagement.model.SubTask
import com.dashimaki_dofu.mytaskmanagement.model.SubTaskStatus
import com.dashimaki_dofu.mytaskmanagement.model.Task
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


/**
 * TaskEditViewModel
 *
 * Created by Yoshiyasu on 2024/02/22
 */

abstract class TaskEditViewModel : ViewModel() {
    sealed interface UiState {
        data class TaskState(
            var id: Int = defaultId,
            var title: String = "",
            var deadlineDate: Instant? = null,
            var isTitleValid: Boolean = false,
            var isDeadlineValid: Boolean = false
        ) : UiState {
            constructor(task: Task) : this() {
                id = task.id
                title = task.title
                deadlineDate = task.deadlineDate
                isTitleValid = task.title.isNotEmpty()
                isDeadlineValid = task.formattedDeadLineString.isNotEmpty()
            }

            val formattedDeadlineString: String
                get() {
                    return deadlineDate?.let {
                        val localDateTime =
                            LocalDateTime.ofInstant(deadlineDate, ZoneId.systemDefault())
                        val dateFormatter = DateTimeFormatter.ofPattern("M/d")
                        dateFormatter.format(localDateTime)
                    } ?: ""
                }
        }

        data class SubTaskState(
            var id: Int = defaultId,
            var taskId: Int = 0,
            var title: String = "",
            var status: SubTaskStatus = SubTaskStatus.INCOMPLETE,
            var isValid: Boolean = false
        ) : UiState {
            constructor(subTask: SubTask) : this() {
                id = subTask.id
                taskId = subTask.taskId
                title = subTask.title
                status = subTask.status
                isValid = subTask.title.isNotEmpty()
            }
        }
    }

    var deleteSubTaskIds: MutableList<Int> = mutableListOf()

    open val taskState: StateFlow<UiState.TaskState> = MutableStateFlow(UiState.TaskState())
    open val subTaskStateList: StateFlow<MutableList<UiState.SubTaskState>> = MutableStateFlow(
        mutableListOf()
    )
    open val showDatePickerState: StateFlow<Boolean> = MutableStateFlow(false)
    open val showAlertDialogState: StateFlow<Boolean> = MutableStateFlow(false)

    open fun loadTaskSubject(taskId: Int?) = Unit
    open fun updateTaskTitle(title: String) = Unit
    open fun updateTaskDeadline(dateMillis: Long?) = Unit
    open fun saveTask(completion: (() -> Unit)?) = Unit
    open fun addSubTaskItem() = Unit
    open fun deleteSubTaskItem(index: Int) = Unit
    open fun updateSubTaskTitle(index: Int, title: String) = Unit
    open fun updateSubTaskStatus(index: Int, status: SubTaskStatus) = Unit

    open fun showDatePicker() = Unit
    open fun dismissDatePicker() = Unit
    open fun showAlertDialog() = Unit
    open fun dismissAlertDialog() = Unit
}

@HiltViewModel
class TaskEditViewModelImpl @Inject constructor(
    private val taskSubjectRepository: TaskSubjectRepository
) : TaskEditViewModel() {
    private val _taskState = MutableStateFlow(UiState.TaskState())
    override val taskState = _taskState.asStateFlow()

    private val _subTaskStateList = MutableStateFlow<MutableList<UiState.SubTaskState>>(mutableStateListOf())
    override val subTaskStateList = _subTaskStateList.asStateFlow()

    private val _showDatePickerState = MutableStateFlow(false)
    override val showDatePickerState = _showDatePickerState.asStateFlow()

    private val _showAlertDialogState = MutableStateFlow(false)
    override val showAlertDialogState = _showAlertDialogState.asStateFlow()

    override fun loadTaskSubject(taskId: Int?) {
        viewModelScope.launch {
            taskId?.let {
                val taskSubject = taskSubjectRepository.getTaskSubject(it)
                _taskState.value = UiState.TaskState(task = taskSubject.task)
                val subTaskStates = taskSubject.subTasks.map { subTask ->
                    UiState.SubTaskState(subTask = subTask)
                }.toMutableStateList()
                _subTaskStateList.value = subTaskStates
            }
        }
    }

    override fun updateTaskTitle(title: String) {
        _taskState.update {
            it.copy(
                title = title,
                isTitleValid = title.isNotEmpty()
            )
        }
    }

    override fun updateTaskDeadline(dateMillis: Long?) {
        _taskState.update {
            val deadlineInstant = dateMillis?.let { millis ->
                Instant.ofEpochMilli(millis)
            }
            it.copy(
                deadlineDate = deadlineInstant,
                isDeadlineValid = deadlineInstant != null
            )
        }
    }

    override fun saveTask(completion: (() -> Unit)?) {
        viewModelScope.launch {
            // 課題を登録or更新後、
            val task = Task()
            task.id = _taskState.value.id
            task.title = _taskState.value.title
            task.deadline = _taskState.value.deadline
            val taskId = taskSubjectRepository.saveTask(task)

            // 子課題を登録する
            _subTaskStateList.value.forEach {
                val subTask = SubTask()
                subTask.id = it.id
                subTask.taskId = taskId
                subTask.title = it.title
                subTask.status = it.status
                taskSubjectRepository.saveSubTask(subTask)
            }

            // 削除候補の子課題があれば削除する
            deleteSubTaskIds.forEach {
                taskSubjectRepository.deleteSubTask(it)
            }
            deleteSubTaskIds = mutableListOf()
            completion?.invoke()
        }
    }

    override fun addSubTaskItem() {
        _subTaskStateList.update {
            (it + UiState.SubTaskState()).toMutableStateList()
        }
    }

    override fun deleteSubTaskItem(index: Int) {
        _subTaskStateList.update {
            val deleted = it.removeAt(index)
            // 削除した子課題が作成済のものであれば削除候補に入れる
            if (deleted.taskId != defaultId) {
                deleteSubTaskIds.add(deleted.id)
            }
            it.toMutableStateList()
        }
    }

    override fun updateSubTaskTitle(index: Int, title: String) {
        _subTaskStateList.update { subTaskStates ->
            subTaskStates.getOrNull(index)?.also {
                it.title = title
                it.isValid = title.isNotEmpty()
            }
            subTaskStates.toMutableStateList()
        }
    }

    override fun updateSubTaskStatus(index: Int, status: SubTaskStatus) {
        _subTaskStateList.update { subTaskStates ->
            subTaskStates.getOrNull(index)?.also {
                it.status = status
            }
            subTaskStates.toMutableStateList()
        }
    }

    override fun showDatePicker() {
        _showDatePickerState.value = true
    }

    override fun showAlertDialog() {
        _showAlertDialogState.value = true
    }

    override fun dismissDatePicker() {
        _showDatePickerState.value = false
    }

    override fun dismissAlertDialog() {
        _showAlertDialogState.value = false
    }
}

class TaskEditViewModelMock : TaskEditViewModel() {
    override val taskState: StateFlow<UiState.TaskState>
        get() = MutableStateFlow(UiState.TaskState())
    override val subTaskStateList: StateFlow<MutableList<UiState.SubTaskState>>
        get() = MutableStateFlow(mutableListOf(UiState.SubTaskState(title = "子課題")))
}

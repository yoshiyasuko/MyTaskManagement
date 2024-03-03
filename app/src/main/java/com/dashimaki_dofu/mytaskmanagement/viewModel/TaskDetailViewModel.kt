package com.dashimaki_dofu.mytaskmanagement.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

abstract class TaskDetailViewModel : ViewModel() {
    sealed interface UiState {
        data object Loading : UiState
        data class Loaded(
            val taskSubject: TaskSubject
        ) : UiState
    }

    abstract val uiState: StateFlow<UiState>
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
    private val _uiState = MutableStateFlow<UiState>(Loading)
    override val uiState = _uiState.asStateFlow()

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
}

class TaskDetailViewModelMock : TaskDetailViewModel() {
    override val uiState: StateFlow<UiState>
        get() = MutableStateFlow(
            Loaded(
                makeDummyTaskSubjects()
                    .first()
            )
        )
}


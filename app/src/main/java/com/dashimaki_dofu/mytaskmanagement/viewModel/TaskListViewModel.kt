package com.dashimaki_dofu.mytaskmanagement.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyTaskSubjects
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepository
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskListViewModel.UiState.Loaded
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskListViewModel.UiState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * TaskListViewModel
 *
 * Created by Yoshiyasu on 2024/02/13
 */

//region TaskListViewModel(abstract class)
abstract class TaskListViewModel : ViewModel() {
    sealed interface UiState {
        data object Loading : UiState
        data class Loaded(
            val taskSubjects: List<TaskSubject>
        ) : UiState
    }

    abstract val uiState: StateFlow<UiState>

    open fun fetchTaskSubjects() = Unit
}
//endregion

//region Impl
@HiltViewModel
class TaskListViewModelImpl @Inject constructor(
    private val taskSubjectRepository: TaskSubjectRepository,
) : TaskListViewModel() {
    private val _uiState = MutableStateFlow<UiState>(Loading)
    override val uiState = _uiState.asStateFlow()

    override fun fetchTaskSubjects() {
        viewModelScope.launch {
            val taskSubjects = taskSubjectRepository.getAllTaskSubjects()
            _uiState.value = Loaded(taskSubjects)
        }
    }
}
//endregion

//region Mock
class TaskListViewModelMock : TaskListViewModel() {
    override val uiState: StateFlow<UiState>
        get() = MutableStateFlow(Loaded(makeDummyTaskSubjects()))
}
//endregion

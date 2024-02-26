package com.dashimaki_dofu.mytaskmanagement.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyTaskSubjects
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepository
import com.google.firebase.appdistribution.ktx.appDistribution
import com.google.firebase.ktx.Firebase
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

abstract class TaskListViewModel : ViewModel() {
    abstract val taskSubjects: StateFlow<List<TaskSubject>>

    open fun fetchTaskSubjects() = Unit
    open fun showFeedbackSend() = Unit
}

@HiltViewModel
class TaskListViewModelImpl @Inject constructor(
    private val taskSubjectRepository: TaskSubjectRepository,
) : TaskListViewModel() {
    private val _taskSubjects = MutableStateFlow<List<TaskSubject>>(emptyList())
    override val taskSubjects = _taskSubjects.asStateFlow()

    override fun fetchTaskSubjects() {
        viewModelScope.launch {
            _taskSubjects.value = taskSubjectRepository.getAllTaskSubjects()
        }
    }

    override fun showFeedbackSend() {
        Firebase.appDistribution.startFeedback("フィードバックを送信")
    }
}

class TaskListViewModelMock : TaskListViewModel() {
    override val taskSubjects: StateFlow<List<TaskSubject>>
        get() = MutableStateFlow(makeDummyTaskSubjects())
}

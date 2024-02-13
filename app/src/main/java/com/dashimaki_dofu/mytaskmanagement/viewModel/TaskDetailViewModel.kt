package com.dashimaki_dofu.mytaskmanagement.viewModel

import androidx.lifecycle.ViewModel
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject


/**
 * TaskDetailViewModel
 *
 * Created by Yoshiyasu on 2024/02/13
 */

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskSubjectRepository: TaskSubjectRepository,
) : ViewModel() {
    private val taskId = MutableStateFlow(-1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val taskSubject: Flow<TaskSubject> = taskId.flatMapLatest {
        taskId -> taskSubjectRepository.getTaskSubject(taskId)
    }

    fun setTaskId(taskId: Int) {
        this.taskId.value = taskId
    }
}


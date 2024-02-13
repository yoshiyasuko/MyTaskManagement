package com.dashimaki_dofu.mytaskmanagement.viewModel

import androidx.lifecycle.ViewModel
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val taskSubjects = taskSubjectRepository.getAllTaskSubjects()
}

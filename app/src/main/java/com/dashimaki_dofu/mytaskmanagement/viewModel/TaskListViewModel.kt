package com.dashimaki_dofu.mytaskmanagement.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepository


/**
 * TaskListViewModel
 *
 * Created by Yoshiyasu on 2024/02/13
 */

class TaskListViewModel(
    private val taskSubjectRepository: TaskSubjectRepository,
    val taskSubjects: MutableLiveData<List<TaskSubject>>
) : ViewModel() {
}

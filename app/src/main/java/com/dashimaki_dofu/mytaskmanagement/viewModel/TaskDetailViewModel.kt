package com.dashimaki_dofu.mytaskmanagement.viewModel

import androidx.lifecycle.ViewModel
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepository


/**
 * TaskDetailViewModel
 *
 * Created by Yoshiyasu on 2024/02/13
 */

class TaskDetailViewModel(
    private val taskSubjectRepository: TaskSubjectRepository,
    val taskSubject: TaskSubject
) : ViewModel() {
}

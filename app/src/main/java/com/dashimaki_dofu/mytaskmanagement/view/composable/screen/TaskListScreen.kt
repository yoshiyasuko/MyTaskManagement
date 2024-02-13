package com.dashimaki_dofu.mytaskmanagement.view.composable.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyTaskSubjects
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepository
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepositoryImpl
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepositoryMock
import com.dashimaki_dofu.mytaskmanagement.view.composable.TaskList
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskListViewModel


/**
 * TaskListScreen
 *
 * Created by Yoshiyasu on 2024/02/10
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    taskSubjects: List<TaskSubject>,
    taskSubjectRepository: TaskSubjectRepository = TaskSubjectRepositoryImpl(),
    taskListViewModel: TaskListViewModel = viewModel { TaskListViewModel(
        taskSubjectRepository = taskSubjectRepository,
        taskSubjects = MutableLiveData(taskSubjects)
    ) },
    onClickItem: (id: Int) -> Unit
) {
    val taskSubjectsState = taskListViewModel.taskSubjects.observeAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("課題")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                taskSubjectsState.value?.let {
                    TaskList(
                        taskSubjects = it,
                        onClickItem = onClickItem
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListScreenPreview() {
    TaskListScreen(
        taskSubjects = makeDummyTaskSubjects(),
        taskSubjectRepository = TaskSubjectRepositoryMock(),
        onClickItem = {}
    )
}

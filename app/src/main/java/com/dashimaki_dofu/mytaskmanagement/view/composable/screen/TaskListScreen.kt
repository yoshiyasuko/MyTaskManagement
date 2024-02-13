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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyTaskSubjects
import com.dashimaki_dofu.mytaskmanagement.view.composable.TaskList
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskListViewModel


/**
 * TaskListScreen
 *
 * Created by Yoshiyasu on 2024/02/10
 */

@Composable
fun TaskListScreen(
    taskListViewModel: TaskListViewModel,
    onClickItem: (id: Int) -> Unit
) {
    val taskSubjects = taskListViewModel.taskSubjects.collectAsState(initial = emptyList())

    TaskListScreen(
        taskSubjects = taskSubjects.value,
        onClickItem = onClickItem
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    taskSubjects: List<TaskSubject>,
    onClickItem: (id: Int) -> Unit
) {
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
                TaskList(
                    taskSubjects = taskSubjects,
                    onClickItem = onClickItem
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListScreenPreview() {
    TaskListScreen(
        taskSubjects = makeDummyTaskSubjects(),
        onClickItem = {}
    )
}

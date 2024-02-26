package com.dashimaki_dofu.mytaskmanagement.view.composable.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dashimaki_dofu.mytaskmanagement.view.composable.TaskList
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskListViewModel
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskListViewModelMock


/**
 * TaskListScreen
 *
 * Created by Yoshiyasu on 2024/02/10
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel,
    onClickItem: (id: Int) -> Unit,
    onClickAddTaskButton: () -> Unit
) {
    val taskSubjects = viewModel.taskSubjects.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.fetchTaskSubjects()
    }

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
                    ),
                    actions = {
                        IconButton(
                            onClick = onClickAddTaskButton
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "go back"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
                    .fillMaxSize()
            ) {
                if (taskSubjects.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 8.dp),
                        textAlign = TextAlign.Center,
                        text = "課題がありません。\n右上の+ボタンから課題を登録してください。"
                    )
                } else {
                    TaskList(
                        taskSubjects = taskSubjects,
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
        viewModel = TaskListViewModelMock(),
        onClickItem = {},
        onClickAddTaskButton = {}
    )
}

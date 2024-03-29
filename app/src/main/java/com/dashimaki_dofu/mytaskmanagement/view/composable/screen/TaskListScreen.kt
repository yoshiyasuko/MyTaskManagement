package com.dashimaki_dofu.mytaskmanagement.view.composable.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dashimaki_dofu.mytaskmanagement.R
import com.dashimaki_dofu.mytaskmanagement.view.composable.TaskList
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskListViewModel
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskListViewModel.UiState.Loaded
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskListViewModel.UiState.Loading
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
    onClickAddTaskButton: () -> Unit,
    onClickSendFeedback: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    var feedbackMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchTaskSubjects()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            //region Scaffold Contents
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.taskList_title))
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
                        IconButton(
                            onClick = {
                                feedbackMenuExpanded = !feedbackMenuExpanded
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "feedback"
                            )
                        }
                        DropdownMenu(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp)),
                            expanded = feedbackMenuExpanded,
                            onDismissRequest = {
                                feedbackMenuExpanded = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.common_feedback_send))
                                },
                                onClick = {
                                    onClickSendFeedback.invoke()
                                    feedbackMenuExpanded = false
                                }
                            )
                        }
                    }
                )
            }
            //endregion
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                when (uiState) {
                    is Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center),
                        )
                    }

                    is Loaded -> {
                        val taskSubjects = uiState.taskSubjects
                        if (taskSubjects.isEmpty()) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(horizontal = 8.dp),
                                textAlign = TextAlign.Center,
                                text = stringResource(id = R.string.taskList_emptyTask)
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
    }
}


@Preview(showBackground = true)
@Composable
fun TaskListScreenPreview() {
    TaskListScreen(
        viewModel = TaskListViewModelMock(),
        onClickItem = {},
        onClickAddTaskButton = {},
        onClickSendFeedback = {}
    )
}

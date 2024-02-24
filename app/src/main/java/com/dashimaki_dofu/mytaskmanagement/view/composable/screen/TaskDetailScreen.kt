package com.dashimaki_dofu.mytaskmanagement.view.composable.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dashimaki_dofu.mytaskmanagement.database.defaultId
import com.dashimaki_dofu.mytaskmanagement.model.SubTask
import com.dashimaki_dofu.mytaskmanagement.model.makeDummySubTasks
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskDetailViewModel
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskDetailViewModelMock


/**
 * TaskDetailScreen
 *
 * Created by Yoshiyasu on 2024/02/10
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Int,
    viewModel: TaskDetailViewModel,
    onClickNavigationIcon: () -> Unit,
    onClickEditButton: () -> Unit,
    onDeleteCompleted: () -> Unit
) {
    val taskSubject = viewModel.taskSubject.collectAsState().value

    val showDeleteAlertDialog = viewModel.showDeleteAlertDialogState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.fetchTaskSubject(taskId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = taskSubject.task.title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    navigationIcon = {
                        IconButton(onClick = onClickNavigationIcon) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "go back"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = onClickEditButton
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "edit"
                            )
                        }
                        IconButton(
                            onClick = {
                                viewModel.showDeleteAlertDialog()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "delete"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                if (taskSubject.task.id == defaultId) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                taskSubject.task.color
                                    .copy(alpha = 0.3f)
                                    .compositeOver(Color.White)
                            )
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(all = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row {
                                Text(
                                    text = "締切: ${taskSubject.task.formattedDeadLineString}",
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 28.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            if (taskSubject.subTasks.isEmpty()) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = "子課題がありません\n右上の編集ボタンから子課題を登録しましょう",
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .height(60.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .border(
                                            width = 4.dp,
                                            color = taskSubject.task.color,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(taskSubject.progressRate)
                                            .fillMaxHeight()
                                            .background(color = taskSubject.task.color)
                                    )
                                    Text(
                                        text = taskSubject.progressRateString,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = taskSubject.progressRateStringColor,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                LazyColumn {
                                    items(taskSubject.subTasks) { subTask ->
                                        SubTaskListItem(subTask = subTask)
                                    }
                                }
                            }
                        }
                    }
                    if (showDeleteAlertDialog) {
                        AlertDialog(
                            onDismissRequest = {
                                viewModel.dismissDeleteAlertDialog()
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        viewModel.deleteTask(
                                            taskSubject.task.id,
                                            completion = onDeleteCompleted
                                        )
                                    }
                                ) {
                                    Text(text = "OK")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        viewModel.dismissDeleteAlertDialog()
                                    }
                                ) {
                                    Text(text = "キャンセル")
                                }
                            },
                            title = {
                                Text(text = "${taskSubject.task.title}を削除します。よろしいですか？")
                            },
                            text = {
                                Text(text = "この操作は元に戻せません。")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubTaskListItem(subTask: SubTask) {
    Row(
        modifier = Modifier
            .padding(
                vertical = 12.dp,
                horizontal = 8.dp
            )
            .height(60.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "・${subTask.title}",
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
        )
        Text(text = ":")
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val resourceId = subTask.status.stampResourceId) {
                null -> Text(text = "着手中")
                else -> Image(
                    painter = painterResource(id = resourceId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDetailScreenPreview() {
    TaskDetailScreen(
        taskId = 0,
        viewModel = TaskDetailViewModelMock(),
        onClickEditButton = {},
        onClickNavigationIcon = {},
        onDeleteCompleted = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SubTaskListItemPreview() {
    SubTaskListItem(subTask = makeDummySubTasks().first())
}

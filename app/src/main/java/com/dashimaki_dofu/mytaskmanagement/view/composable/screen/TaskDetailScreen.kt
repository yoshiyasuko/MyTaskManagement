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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dashimaki_dofu.mytaskmanagement.R
import com.dashimaki_dofu.mytaskmanagement.model.SubTask
import com.dashimaki_dofu.mytaskmanagement.model.Task
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.model.makeDummySubTasks
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyTaskSubjects
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskDetailViewModel


/**
 * TaskDetailScreen
 *
 * Created by Yoshiyasu on 2024/02/10
 */

@Composable
fun TaskDetailScreen(
    taskDetailViewModel: TaskDetailViewModel,
    onClickNavigationIcon: () -> Unit
) {
    val taskSubject = taskDetailViewModel.taskSubject.collectAsState(makeEmptyTaskSubject())
    
    TaskDetailScreen(taskSubject = taskSubject.value, onClickNavigationIcon)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskSubject: TaskSubject,
    onClickNavigationIcon: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(taskSubject.task.title)
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
                    }
                )
            }
        ) { innerPadding ->
            if (taskSubject.task.id == emptyTaskId) {
                CircularProgressIndicator()
            } else {
                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
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
                            ConstraintLayout(
                                modifier = Modifier
                                    .height(60.dp)
                                    .border(
                                        width = 4.dp,
                                        color = taskSubject.task.color,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .fillMaxWidth()
                            ) {
                                val (progressBarRef, progressTextRef) = createRefs()

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(taskSubject.progressRate)
                                        .fillMaxHeight()
                                        .border(
                                            width = 0.dp,
                                            color = Color.White,
                                            shape = RoundedCornerShape(
                                                topStart = 12.dp,
                                                bottomStart = 12.dp
                                            )
                                        )
                                        .background(
                                            color = taskSubject.task.color,
                                            shape = RoundedCornerShape(
                                                topStart = 12.dp,
                                                bottomStart = 12.dp
                                            )
                                        )
                                        .constrainAs(progressBarRef) {}
                                )
                                Text(
                                    text = "${(taskSubject.progressRate * 100).toInt()}%",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.lightGray1),
                                    modifier = Modifier
                                        .constrainAs(progressTextRef) {
                                            bottom.linkTo(progressBarRef.bottom, margin = 2.dp)
                                            centerAround(progressBarRef.absoluteRight)
                                        }
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
            }
        }
    }
}

private const val emptyTaskId = -1

fun makeEmptyTaskSubject(): TaskSubject {
    val taskSubject = TaskSubject()
    taskSubject.task = Task()
    taskSubject.subTasks = emptyList()
    return taskSubject
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
            when (val resourceId = subTask.stampResourceId) {
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
        taskSubject = makeDummyTaskSubjects().first(),
        onClickNavigationIcon = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SubTaskListItemPreview() {
    SubTaskListItem(subTask = makeDummySubTasks().first { it.id == 2 })
}

package com.dashimaki_dofu.mytaskmanagement.view.composable.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dashimaki_dofu.mytaskmanagement.R
import com.dashimaki_dofu.mytaskmanagement.model.SubTaskStatus
import com.dashimaki_dofu.mytaskmanagement.view.composable.TimePickerDialog
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskEditViewModel
import com.dashimaki_dofu.mytaskmanagement.viewModel.TaskEditViewModelMock
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset


/**
 * TaskEditScreen
 *
 * Created by Yoshiyasu on 2024/02/22
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    taskId: Int? = null,
    viewModel: TaskEditViewModel,
    onClickNavigationIcon: () -> Unit,
    onSaveCompleted: () -> Unit
) {
    val task = viewModel.taskState.collectAsState()
    val subTasks = viewModel.subTaskStateList.collectAsState()

    val showDatePickerState = viewModel.showDatePickerState.collectAsState()
    val showTimePickerState = viewModel.showTimePickerState.collectAsState()
    val showSaveErrorDialogState = viewModel.showAlertDialogState.collectAsState()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .atStartOfDay()
            .toEpochSecond(ZoneOffset.UTC)
            .times(1000)
    )
    val timePickerState = rememberTimePickerState()

    LaunchedEffect(Unit) {
        viewModel.loadTaskSubject(taskId)
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
                            text = stringResource(
                                id = if (taskId == null) {
                                    R.string.taskEdit_title_create
                                } else {
                                    R.string.taskEdit_title_edit
                                }
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = onClickNavigationIcon
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "go back"
                            )
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                if (!task.value.isTitleValid ||
                                    !task.value.isDeadlineValid ||
                                    subTasks.value.any { !it.isValid }
                                ) {
                                    viewModel.showAlertDialog()
                                } else {
                                    viewModel.saveTask(completion = onSaveCompleted)
                                }
                            }
                        ) {
                            Text(text = stringResource(id = R.string.common_save))
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    item {
                        Text(
                            modifier = Modifier
                                .padding(bottom = 4.dp),
                            text = stringResource(id = R.string.taskEdit_input_taskTitle)
                        )
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            singleLine = true,
                            value = task.value.title,
                            onValueChange = {
                                viewModel.updateTaskTitle(it)
                            }
                        )
                        Text(
                            modifier = Modifier
                                .padding(bottom = 4.dp),
                            text = stringResource(id = R.string.taskEdit_input_deadlineDate)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.showDatePicker()
                                }
                        ) {
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                singleLine = true,
                                enabled = false,
                                value = task.value.formattedDeadlineString,
                                onValueChange = {}
                            )
                        }
                        Text(
                            modifier = Modifier
                                .padding(bottom = 4.dp),
                            text = stringResource(id = R.string.taskEdit_input_deadlineTime)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextField(
                                modifier = Modifier
                                    .clickable {
                                        viewModel.showTimePicker()
                                    }
                                    .weight(1f),
                                singleLine = true,
                                enabled = false,
                                value = task.value.formattedDeadlineTimeString,
                                onValueChange = {}
                            )
                            IconButton(
                                modifier = Modifier
                                    .size(48.dp),
                                onClick = { viewModel.clearTaskDeadlineTime() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete deadline time"
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            modifier = Modifier
                                .padding(bottom = 16.dp),
                            onClick = {
                                viewModel.addSubTaskItem()
                            }
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                text = stringResource(id = R.string.taskEdit_addSubTaskButton)
                            )
                        }

                        if (showDatePickerState.value) {
                            DatePickerDialog(
                                onDismissRequest = { viewModel.dismissDatePicker() },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            viewModel.dismissDatePicker()
                                            viewModel.updateTaskDeadline(datePickerState.selectedDateMillis)
                                        }
                                    ) {
                                        Text(text = stringResource(id = R.string.common_decide))
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            viewModel.dismissDatePicker()
                                        }
                                    ) {
                                        Text(text = stringResource(id = R.string.common_cancel))
                                    }
                                }
                            ) {
                                DatePicker(state = datePickerState)
                            }
                        }

                        if (showTimePickerState.value) {
                            TimePickerDialog(
                                timePickerState = timePickerState,
                                onDismissRequest = { viewModel.dismissTimePicker() },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            viewModel.dismissTimePicker()
                                            viewModel.updateTaskDeadlineTime(
                                                timePickerState.hour,
                                                timePickerState.minute
                                            )
                                        }
                                    ) {
                                        Text(text = stringResource(id = R.string.common_decide))
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            viewModel.dismissTimePicker()
                                        }
                                    ) {
                                        Text(text = stringResource(id = R.string.common_cancel))
                                    }
                                }
                            )
                        }

                        if (showSaveErrorDialogState.value) {
                            AlertDialog(
                                onDismissRequest = {
                                    viewModel.dismissAlertDialog()
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            viewModel.dismissAlertDialog()
                                        }
                                    ) {
                                        Text(text = stringResource(id = R.string.common_ok))
                                    }
                                },
                                title = {
                                    Text(text = stringResource(id = R.string.taskEdit_saveTaskErrorDialog_title))
                                },
                                text = {
                                    Column {
                                        if (!task.value.isTitleValid) {
                                            Text(
                                                text = stringResource(
                                                    id = R.string.taskEdit_saveTaskErrorDialog_taskTitleNotValid
                                                )
                                            )
                                        }
                                        if (!task.value.isDeadlineValid) {
                                            Text(
                                                text = stringResource(
                                                    id = R.string.taskEdit_saveTaskErrorDialog_taskDeadlineDateNotValid
                                                )
                                            )
                                        }
                                        subTasks.value.forEachIndexed { index, subTask ->
                                            if (!subTask.isValid) {
                                                Text(
                                                    text = stringResource(
                                                        id = R.string.taskEdit_saveTaskErrorDialog_subTaskTitleNotValid,
                                                        index + 1
                                                    )
                                                )
                                            }
                                        }
                                    }
                                },
                                dismissButton = null
                            )
                        }
                    }

                    itemsIndexed(subTasks.value) { index, subTaskState ->
                        SubTaskEditItem(
                            index = index,
                            subTaskState = subTaskState,
                            onValueChanged = {
                                viewModel.updateSubTaskTitle(index, it)
                            },
                            onClickDeleteButton = {
                                viewModel.deleteSubTaskItem(index)
                            },
                            onStatusSelected = { selectedStatus ->
                                viewModel.updateSubTaskStatus(index, selectedStatus)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubTaskEditItem(
    index: Int,
    subTaskState: TaskEditViewModel.UiState.SubTaskState,
    onValueChanged: (String) -> Unit,
    onClickDeleteButton: () -> Unit,
    onStatusSelected: (SubTaskStatus) -> Unit
) {
    var statusMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = 4.dp),
            text = stringResource(
                id = R.string.taskEdit_subTask_title,
                index + 1
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f),
                singleLine = true,
                value = subTaskState.title,
                onValueChange = onValueChanged
            )
            val statusButtonWidth = 60.dp
            val statusButtonHeight = 48.dp
            Box(
                modifier = Modifier
                    .width(statusButtonWidth)
                    .height(statusButtonHeight)
                    .padding(start = 4.dp)
            ) {
                when (val resourceId = subTaskState.status.stampResourceId) {
                    null -> {
                        TextButton(
                            modifier = Modifier
                                .align(Alignment.Center),
                            onClick = {
                                statusMenuExpanded = true
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.subTask_status_active),
                                fontSize = 10.sp
                            )
                        }
                    }

                    else -> {
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.Center),
                            onClick = {
                                statusMenuExpanded = true
                            }
                        ) {
                            Image(
                                painter = painterResource(id = resourceId),
                                contentDescription = "button"
                            )
                        }
                    }
                }
                DropdownMenu(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp)),
                    expanded = statusMenuExpanded,
                    onDismissRequest = {
                        statusMenuExpanded = false
                    }
                ) {
                    SubTaskStatus.entries.forEach {
                        DropdownMenuItem(
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 8.dp),
                            text = {
                                Text(
                                    text = stringResource(id = it.stringResourceId),
                                    fontSize = 16.sp
                                )
                            },
                            onClick = {
                                onStatusSelected.invoke(it)
                                statusMenuExpanded = false
                            }
                        )
                    }
                }
            }
            IconButton(
                modifier = Modifier
                    .size(48.dp),
                onClick = onClickDeleteButton
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskEditScreenPreview() {
    TaskEditScreen(
        viewModel = TaskEditViewModelMock(),
        onClickNavigationIcon = {},
        onSaveCompleted = {}
    )
}

@Preview(showBackground = true)
@Composable
fun SubTaskEditItemPreview() {
    SubTaskEditItem(
        index = 0,
        subTaskState = TaskEditViewModel.UiState.SubTaskState(status = SubTaskStatus.ACTIVE),
        onValueChanged = {},
        onClickDeleteButton = {},
        onStatusSelected = {}
    )
}

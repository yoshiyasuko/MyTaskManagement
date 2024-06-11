package com.dashimaki_dofu.mytaskmanagement.view.composable.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.dashimaki_dofu.mytaskmanagement.R
import com.dashimaki_dofu.mytaskmanagement.ui.theme.TaskColor


/**
 * TaskSelectColorScreen
 *
 * Created by Yoshiyasu on 2024/06/08
 */

@Composable
fun TaskSelectColorDialog(
    onDismiss: () -> Unit,
    currentTaskColor: TaskColor,
    onItemClick: (TaskColor) -> Unit,
) {
    val dialogWidth = 300.dp
    val itemSpace = 8.dp
    var selectedTaskColorState by remember { mutableStateOf(currentTaskColor) }

    AlertDialog(
        modifier = Modifier
            .width(dialogWidth),
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        onDismissRequest = onDismiss,
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(itemSpace)
            ) {
                item {
                    Text(text = stringResource(id = R.string.taskEdit_selectColorDialog_title))
                }

                items(
                    items = TaskColor.entries.chunked(3)
                ) { taskColorListOfRow ->
                    TaskSelectColorGridRow(
                        itemSpace = itemSpace,
                        dialogWidth = dialogWidth,
                        taskColorListOfRow = taskColorListOfRow,
                        selectedTaskColor = selectedTaskColorState,
                        onItemClick = {
                            selectedTaskColorState = it
                            onItemClick.invoke(it)
                        }
                    )
                }
            }
        },
        confirmButton = {},
    )
}

@Composable
fun TaskSelectColorGridRow(
    itemSpace: Dp,
    dialogWidth: Dp,
    taskColorListOfRow: List<TaskColor>,
    selectedTaskColor: TaskColor,
    onItemClick: (TaskColor) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(itemSpace)
    ) {
        taskColorListOfRow.forEach {
            TaskSelectColorItem(
                isSelected = selectedTaskColor == it,
                // FIXME: dialogのpaddingを取得する方法が微妙なため、一旦固定値で計算。
                itemSize = ((dialogWidth.value - 2 * 16 - 2 * itemSpace.value - 20) / 3).dp,
                taskColor = it,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun TaskSelectColorItem(
    isSelected: Boolean,
    itemSize: Dp,
    taskColor: TaskColor,
    onItemClick: (TaskColor) -> Unit
) {
    BoxWithConstraints {
        Box(
            modifier = Modifier
                .size(itemSize)
                .background(color = taskColor.color)
                .border(
                    width = if (isSelected) 4.dp else 0.dp,
                    color = Color.Black
                )
                .clickable {
                    onItemClick.invoke(taskColor)
                }
        )
    }
}

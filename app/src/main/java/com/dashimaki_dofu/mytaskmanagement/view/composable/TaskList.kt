package com.dashimaki_dofu.mytaskmanagement.view.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dashimaki_dofu.mytaskmanagement.model.Task
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyTasks


/**
 * TaskList
 *
 * Created by Yoshiyasu on 2024/02/10
 */

@Composable
fun TaskList(tasks: List<Task>, onClickItem: (id: Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            tasks,
            key = { task -> task.id }
        ) { task ->
            TaskListItem(task = task, onClick = onClickItem)
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TaskListPreview() {
    TaskList(makeDummyTasks(), onClickItem = { })
}

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
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyAllTaskAndSubTasks
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject


/**
 * TaskList
 *
 * Created by Yoshiyasu on 2024/02/10
 */

@Composable
fun TaskList(taskSubjects: List<TaskSubject>, onClickItem: (id: Int) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            taskSubjects,
            key = { taskSubject -> taskSubject.task.id }
        ) { task ->
            TaskListItem(taskSubject = task, onClick = onClickItem)
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TaskListPreview() {
    TaskList(makeDummyTaskSubjects(), onClickItem = { })
}

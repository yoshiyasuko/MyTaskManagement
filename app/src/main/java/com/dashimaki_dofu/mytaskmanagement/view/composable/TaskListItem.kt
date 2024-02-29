package com.dashimaki_dofu.mytaskmanagement.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dashimaki_dofu.mytaskmanagement.R
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyTaskSubjects


/**
 * TaskListItem
 *
 * Created by Yoshiyasu on 2024/02/05
 */

@Composable
fun TaskListItem(taskSubject: TaskSubject, onClick: (id: Int) -> Unit) {
    Box(
        modifier = Modifier
            .clickable {
                onClick(taskSubject.task.id)
            }
            .height(64.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                taskSubject.task.color
                    .copy(alpha = 0.3f)
                    .compositeOver(Color.White)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(taskSubject.task.color)
                .fillMaxWidth(taskSubject.progressRate)
        )
        Box(
            modifier = Modifier.padding(all = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = taskSubject.task.title,
                    modifier = Modifier
                        .weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    color = taskSubject.task.listTitleColor
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        modifier = Modifier
                            .width(68.dp),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = taskSubject.progressRateStringColor,
                        textAlign = TextAlign.End,
                        text = taskSubject.progressRateString
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = stringResource(id = R.string.taskList_deadlineDate),
                            fontSize = 12.sp
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = taskSubject.task.formattedDeadLineString,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListItemPreview() {
    TaskListItem(
        taskSubject = makeDummyTaskSubjects().first(),
        onClick = { }
    )
}

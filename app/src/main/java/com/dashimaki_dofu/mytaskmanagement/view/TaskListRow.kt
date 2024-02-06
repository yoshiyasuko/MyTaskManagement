package com.dashimaki_dofu.mytaskmanagement.view

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dashimaki_dofu.mytaskmanagement.R
import com.dashimaki_dofu.mytaskmanagement.model.Task
import com.dashimaki_dofu.mytaskmanagement.model.makeDummySubTasks
import java.util.Date


/**
 * TaskListRow
 *
 * Created by Yoshiyasu on 2024/02/05
 */

@Composable
fun TaskListRow(task: Task) {
    Box(
        modifier = Modifier
            .height(60.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                task.color
                    .copy(alpha = 0.3f)
                    .compositeOver(Color.White)
            )
        ,
        contentAlignment = Alignment.Center,
    ) {
        ConstraintLayout {
            val (progressBarRef, progressTextRef) = createRefs()

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(task.color)
                    .fillMaxWidth(task.progressRate)
                    .constrainAs(progressBarRef) {}
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Spacer(modifier = Modifier.width(40.dp))
                        Text(
                            text = task.title,
                            fontSize = 24.sp,
                            color = task.listTitleColor
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = "締切",
                            fontSize = 12.sp
                        )
                        Text(
                            textAlign = TextAlign.Center,
                            text = task.formattedDeadLineString,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    }
                }
            }
            Text(
                text = "${(task.progressRate * 100).toInt()}%",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.lightGray1),
                modifier = Modifier.constrainAs(progressTextRef) {
                    bottom.linkTo(progressBarRef.bottom)
                    centerAround(progressBarRef.absoluteRight)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListRowSample() {
    TaskListRow(
        task = Task(
            title = "世界観",
            color = colorResource(id = R.color.taskColor1),
            subTasks = makeDummySubTasks(),
            deadline = Date()
        )
    )
}

package com.dashimaki_dofu.mytaskmanagement.view.composable.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition.InDate
import com.kizitonwose.calendar.core.DayPosition.MonthDate
import com.kizitonwose.calendar.core.DayPosition.OutDate
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


/**
 * CalendarScreen
 *
 * Created by Yoshiyasu on 2024/03/16
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    val currentMonth = remember { YearMonth.now() }
    val daysOfWeek = remember { daysOfWeek() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    val visibleMonth = rememberFirstMostVisibleMonth(
        state = calendarState,
        viewportPercent = 90f
    )
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            //region Scaffold Contents
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "カレンダー"
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            //endregion
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column {
                    SimpleCalendarTitle(
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 8.dp),
                        currentMonth = visibleMonth.yearMonth,
                        goToPrevious = {
                            coroutineScope.launch {
                                calendarState.animateScrollToMonth(
                                    calendarState.firstVisibleMonth.yearMonth.previousMonth
                                )
                            }
                        },
                        goToNext = {
                            coroutineScope.launch {
                                calendarState.animateScrollToMonth(
                                    calendarState.firstVisibleMonth.yearMonth.nextMonth
                                )
                            }
                        }
                    )
                    HorizontalCalendar(
                        state = calendarState,
                        monthHeader = {
                            DaysOfWeekTitle(daysOfWeek = daysOfWeek)
                        },
                        dayContent = {
                            Day(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleCalendarTitle(
    modifier: Modifier,
    currentMonth: YearMonth,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit,
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CalendarNavigationIcon(
            icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "Previous",
            onClick = goToPrevious,
        )
        Text(
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 15.sp,
            text = currentMonth.displayText(),
            fontWeight = FontWeight.Medium,
        )
        CalendarNavigationIcon(
            icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Next",
            onClick = goToNext,
        )
    }
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.year}年 ${this.month.displayText(short = short)} "
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.getDefault())
}

@Composable
private fun CalendarNavigationIcon(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .clip(shape = CircleShape)
            .clickable(role = Role.Button, onClick = onClick),
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .align(Alignment.Center),
            imageVector = icon,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        daysOfWeek.forEach {
            Text(
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.Center,
                text = it.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = when (it) {
                    SATURDAY -> {
                        Color.Blue
                    }

                    SUNDAY -> {
                        Color.Red
                    }

                    else -> {
                        Color.Unspecified
                    }
                }
            )
        }
    }
}

@Composable
fun Day(day: CalendarDay) {
    val alpha = when (day.position) {
        MonthDate -> {
            1f
        }

        InDate, OutDate -> {
            0.3f
        }
    }
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = when (day.date.dayOfWeek) {
                SATURDAY -> {
                    Color.Blue
                }

                SUNDAY -> {
                    Color.Red
                }

                else -> {
                    Color.Unspecified
                }
            }
        )
    }
}

@Composable
fun rememberFirstMostVisibleMonth(
    state: CalendarState,
    viewportPercent: Float = 50f,
): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.firstMostVisibleMonth(viewportPercent) }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

private fun CalendarLayoutInfo.firstMostVisibleMonth(viewportPercent: Float = 50f): CalendarMonth? {
    return if (visibleMonthsInfo.isEmpty()) {
        null
    } else {
        val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f
        visibleMonthsInfo.firstOrNull { itemInfo ->
            if (itemInfo.offset < 0) {
                itemInfo.offset + itemInfo.size >= viewportSize
            } else {
                itemInfo.size - itemInfo.offset >= viewportSize
            }
        }?.month
    }
}

package com.edozie.reve_jc.ui.screen

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import com.edozie.reve_jc.util.getPriorityColor
import java.time.LocalTime
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edozie.reve_jc.R
import com.edozie.reve_jc.local.model.Task
import com.edozie.reve_jc.local.model.TaskPriority
import com.edozie.reve_jc.ui.widget.TodayTaskCard
import com.edozie.reve_jc.util.getPriorityColor


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayTasksScreen(
    todayTasks: List<Task>,
    onTaskClick: (Task) -> Unit
) {
    val today = remember { LocalDate.now() }
    val dayFormatter = remember { DateTimeFormatter.ofPattern("EEEE, MMMM d") }
    val formattedDate = remember { today.format(dayFormatter) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Today",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (todayTasks.isEmpty()) {
            // 🌟 Empty state with motivational UI
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.no_task_today_ic),
                    contentDescription = "No tasks today",
                    modifier = Modifier.size(120.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "You have no tasks today!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Use this time to recharge, plan ahead, or just relax. You deserve it!",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn {
                items(todayTasks) { task ->
                    TodayTaskCard(task = task, onClick = { onTaskClick(task) })
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TodayTaskScreenPreview() {
    TodayTasksScreen(
        todayTasks = listOf(
            Task(
                title = "Design Login UI",
                description = "Make it clean and minimal",
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(11, 30),
                priority = TaskPriority.HIGH,
                progress = 100
            )
        ),
        onTaskClick = {}
    )
}



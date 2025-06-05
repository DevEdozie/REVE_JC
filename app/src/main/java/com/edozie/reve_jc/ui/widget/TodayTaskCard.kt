package com.edozie.reve_jc.ui.widget

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import com.edozie.reve_jc.util.getPriorityColor
import java.time.LocalDate
import java.time.LocalTime
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edozie.reve_jc.local.model.Task
import com.edozie.reve_jc.local.model.TaskPriority
import com.edozie.reve_jc.util.getPriorityColor

@Composable
fun TodayTaskCard(
    task: Task,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                // Priority color dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            getPriorityColor(task.priority),
                            shape = CircleShape
                        )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = task.description!!,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Time",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${task.startTime} - ${task.endTime}",
                    style = MaterialTheme.typography.labelSmall
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Status icon if task is done
//                if (task.isDone) {
//                    Icon(
//                        Icons.Default.CheckCircle,
//                        contentDescription = "Done",
//                        tint = Color.Green,
//                        modifier = Modifier.size(16.dp)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text("Done", style = MaterialTheme.typography.labelSmall, color = Color.Green)
//                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { task.progress.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50)),
                color = MaterialTheme.colorScheme.primary
            )

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TodayTaskCardPreview() {
    TodayTaskCard(
        task = Task(
            title = "UI Redesign",
            description = "Redesign the onboarding flow and improve task navigation.",
            priority = TaskPriority.HIGH,
            startDate = LocalDate.now(),
            endDate = LocalDate.now().plusDays(3),
            startTime = LocalTime.of(9, 0),
            endTime = LocalTime.of(17, 0),
            progress = 10,
        ),
        onClick = {}
    )
}


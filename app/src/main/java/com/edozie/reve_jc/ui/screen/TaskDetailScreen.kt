package com.edozie.reve_jc.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.edozie.reve_jc.local.model.TaskPriority
import com.edozie.reve_jc.util.getPriorityColor
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskDetailScreen(
    taskTitle: String = "UI Redesign",
    taskDescription: String = "Redesign the onboarding flow and improve task navigation.",
    priority: TaskPriority = TaskPriority.HIGH,
    startDate: LocalDate = LocalDate.now(),
    endDate: LocalDate = LocalDate.now().plusDays(3),
    startTime: LocalTime = LocalTime.of(9, 0),
    endTime: LocalTime = LocalTime.of(17, 0),
    steps: List<Pair<String, Boolean>> = listOf(
        "Sketch wireframes" to true,
        "Build prototype" to false,
        "Collect feedback" to false
    ),
    isTaskDone: Boolean = false,
    onMarkTaskDone: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onToggleStep: (index: Int, checked: Boolean) -> Unit = { _, _ -> }
) {
    val progress = remember(steps) {
        steps.count { it.second }.toFloat() / steps.size.coerceAtLeast(1)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = taskTitle,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = taskDescription,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Priority badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = getPriorityColor(priority),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Priority: ${priority.name}", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Start: $startDate at $startTime")
        Text("End: $endDate at $endTime")

        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar
        Text("Progress", style = MaterialTheme.typography.titleMedium)
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
        )
        Text("${(progress * 100).toInt()}%", modifier = Modifier.padding(top = 4.dp))

        Spacer(modifier = Modifier.height(16.dp))

        // Step checklist
        Text("Steps", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        steps.forEachIndexed { index, (text, isDone) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = isDone,
                    onCheckedChange = { onToggleStep(index, it) }
                )
                Text(
                    text = text,
                    style = if (isDone) MaterialTheme.typography.bodyMedium.copy(color = Color.Gray) else MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mark task done
        Button(
            onClick = onMarkTaskDone,
            enabled = !isTaskDone,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isTaskDone) "Task Completed" else "Mark as Done")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Edit/Delete buttons
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = onEditClick,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Edit")
            }
            Spacer(modifier = Modifier.width(12.dp))
            OutlinedButton(
                onClick = onDeleteClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Delete")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TaskDetailScreenPreview() {
    TaskDetailScreen()
}


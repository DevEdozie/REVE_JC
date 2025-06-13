package com.edozie.reve_jc.ui.screen

import android.os.Build
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.edozie.reve_jc.local.model.TaskPriority
import com.edozie.reve_jc.local.model.TaskStatus
import com.edozie.reve_jc.util.Screen
import com.edozie.reve_jc.util.getPriorityColor
import com.edozie.reve_jc.viewmodel.TaskViewModel
import kotlinx.coroutines.flow.toList
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskDetailScreen(
    vm: TaskViewModel = hiltViewModel(),
    navController: NavController
) {

    val context = LocalContext.current

    val currentTask by vm.currentTask.collectAsState()
    val currentSteps by vm.currentSteps.collectAsState()

    val task = currentTask!!

    val isTaskDone by remember { mutableStateOf(false) }

    // Local UI state to track if we've shown the "all steps done, consider marking complete" toast
    var hasPromptedMarkDone by remember { mutableStateOf(false) }

    // Prepare formatters
    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM d, yyyy") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("h:mm a") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = task.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = task.description!!,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Priority badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = getPriorityColor(task.priority),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Priority: ${task.priority.name}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Formatted dates & times
        val startDate = task.startDate.format(dateFormatter)!!
        val endDate = task.endDate.format(dateFormatter)!!
        val startTime = task.startTime.format(timeFormatter)!!
        val endTime = task.endTime.format(timeFormatter)!!

        Text("Start: $startDate at $startTime")
        Text("End: $endDate at $endTime")

        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar
        Text("Progress", style = MaterialTheme.typography.titleMedium)
        LinearProgressIndicator(
            progress = { (task.progress.coerceIn(0, 100) / 100f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(MaterialTheme.shapes.small),
        )
        Text(
            "${task.progress.coerceIn(0, 100)}%",
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Step checklist
        Text("Steps", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        val steps by currentSteps.collectAsState(initial = emptyList())
        // Safely filter non-null steps
        val nonNullSteps = steps.mapNotNull { it }
        if (nonNullSteps.isEmpty()) {
            Text("No steps added.", color = Color.Gray)
        } else {
            nonNullSteps.forEach { step ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = step.isDone,
                        onCheckedChange = { checked ->
                            // Update this step's isDone
                            val updatedStep = step.copy(isDone = checked)
                            vm.updateStep(updatedStep)
                            // Recompute new progress, update Task:
                            // After repository.updateStep, next emission of currentSteps will reflect change
                            // But we can compute immediately from nonNullSteps + this change:
                            val updatedSteps = nonNullSteps.map {
                                if (it.id == step.id) updatedStep else it
                            }
                            // Compute progress percentage
                            val doneCount = updatedSteps.count { it.isDone }
                            val total = updatedSteps.size
                            val newProgress = if (total > 0) (doneCount * 100 / total) else 0
                            // Determine status: if 100% then DONE, else if >0 then IN_PROGRESS, else TODO
                            val newStatus = when {
                                newProgress >= 100 -> TaskStatus.DONE
                                newProgress > 0 -> TaskStatus.IN_PROGRESS
                                else -> TaskStatus.TODO
                            }
                            val updatedTask = task.copy(progress = newProgress, status = newStatus)
                            vm.updateTaskWithoutAlarm(updatedTask)
                            // If now all steps done, prompt user to mark task complete
                            if (newProgress >= 100 && !hasPromptedMarkDone) {
                                Toast.makeText(
                                    context,
                                    "All steps completed! You can mark the task done.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                hasPromptedMarkDone = true
                            }
                            // If user unchecks leaving <100%, reset prompt flag
                            if (newProgress < 100) {
                                hasPromptedMarkDone = false
                            }
                        }
                    )
                    Text(
                        text = step.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (step.isDone) Color.Gray else Color.Unspecified
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    //
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        // Delete step
                        vm.deleteStep(step)
                        // After deletion, next emission of stepsList will update UI and progress
                        // Optionally, recompute progress now:
                        val remaining = nonNullSteps.filter { it.id != step.id }
                        val doneCount = remaining.count { it.isDone }
                        val total = remaining.size
                        val newProgress = if (total > 0) (doneCount * 100 / total) else 0
                        val newStatus = when {
                            newProgress >= 100 -> TaskStatus.DONE
                            newProgress > 0 -> TaskStatus.IN_PROGRESS
                            else -> TaskStatus.TODO
                        }
                        val updatedTask = task.copy(progress = newProgress, status = newStatus)
                        vm.updateTaskWithoutAlarm(updatedTask)
                    }) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete Step",
                            tint = Color.Red
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Mark task done
            Button(
                onClick = {
                    // If there are steps: mark all as done; else directly mark task done
                    if (nonNullSteps.isNotEmpty()) {
                        // Mark all steps done
                        nonNullSteps.forEach { step ->
                            if (!step.isDone) {
                                vm.updateStep(step.copy(isDone = true))
                            }
                        }
                    }
                    // Update Task: 100% progress, DONE status
                    val updatedTask = task.copy(progress = 100, status = TaskStatus.DONE)
                    vm.updateTaskWithoutAlarm(updatedTask)
                    vm.cancelAlarmsForTaskOnly(task.id)
                    Toast.makeText(context, "Task marked as done", Toast.LENGTH_SHORT).show()
                },
                enabled = task.status != TaskStatus.DONE,
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
                    onClick = {
                        // Navigate to EditTask screen
                        navController.navigate(Screen.EditTask.createRoute(taskId = task.id)) {
                            popUpTo(Screen.TaskDetail.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    onClick = {
                        vm.deleteTask(task)
                        Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
                        navController.popBackStack() // go back after deletion
                    },
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
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TaskDetailScreenPreview() {
    val navController = rememberNavController()
    TaskDetailScreen(navController = navController)
}




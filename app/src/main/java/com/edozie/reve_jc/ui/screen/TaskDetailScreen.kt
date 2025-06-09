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
import androidx.hilt.navigation.compose.hiltViewModel
import com.edozie.reve_jc.local.model.TaskPriority
import com.edozie.reve_jc.util.getPriorityColor
import com.edozie.reve_jc.viewmodel.TaskViewModel
import kotlinx.coroutines.flow.toList
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskDetailScreen(
    vm: TaskViewModel = hiltViewModel()
) {

    val currentTask by vm.currentTask.collectAsState()
    val currentSteps by vm.currentSteps.collectAsState()

    val isTaskDone by remember { mutableStateOf(false) }

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
            text = currentTask?.title!!,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = currentTask?.description!!,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Priority badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .height(10.dp)
                    .width(20.dp)
//                    .size(20.dp)
                    .background(
                        color = getPriorityColor(currentTask?.priority!!),
                        shape = RoundedCornerShape(8.dp)
                    )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Priority: ${currentTask?.priority?.name!!}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Formatted dates & times
        val startDate = currentTask?.startDate?.format(dateFormatter)!!
        val endDate = currentTask?.endDate?.format(dateFormatter)!!
        val startTime = currentTask?.startTime?.format(timeFormatter)!!
        val endTime = currentTask?.endTime?.format(timeFormatter)!!

        Text("Start: ${startDate} at ${startTime}")
        Text("End: ${endDate} at ${endTime}")

        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar
        Text("Progress", style = MaterialTheme.typography.titleMedium)
        LinearProgressIndicator(
            progress = { currentTask?.progress!! / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
        )
        Text(
            "${currentTask?.progress!!}%",
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Step checklist
        Text("Steps", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        val steps by currentSteps.collectAsState(initial = emptyList())
        if (steps.isEmpty()) {
            Text("No steps added.", color = Color.Gray)
        } else {
            steps.forEach { step ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = step?.isDone!!,
                        onCheckedChange = { checked ->
//                            vm.markStepDone(step.copy(isDone = checked))
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
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Mark task done
            Button(
                onClick = {},
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
                    onClick = {},
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    onClick = {},
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
    TaskDetailScreen()
}


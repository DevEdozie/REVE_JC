package com.edozie.reve_jc.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edozie.reve_jc.local.model.Task
import com.edozie.reve_jc.local.model.TaskPriority
import com.edozie.reve_jc.local.model.TaskStatus
import com.edozie.reve_jc.ui.widget.DateTimePickerRow
import com.edozie.reve_jc.ui.widget.PriorityDropdown
import com.edozie.reve_jc.ui.widget.TimePickerRow
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTaskScreen() {

    val context = LocalContext.current

    // State variables
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var startTime by remember { mutableStateOf<LocalTime?>(null) }
    var endTime by remember { mutableStateOf<LocalTime?>(null) }

    var priority by remember { mutableStateOf(TaskPriority.LOW) }
    var steps by remember { mutableStateOf(mutableListOf<String>()) }
    var newStepText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Title Field
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = {
                Text("Task Title")
            },
            placeholder = {
                Text("Enter task title")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description Field
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Task Description") },
            placeholder = {
                Text("Enter task description")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))
        // Start Date & End Date pickers
        DateTimePickerRow(
            startDate, endDate,
            onStartDateSelected = { startDate = it },
            onEndDateSelected = { endDate = it }
        )

        // Start Time & End Time pickers
        TimePickerRow(
            startTime, endTime,
            onStartTimeSelected = { startTime = it },
            onEndTimeSelected = { endTime = it }
        )

        // Priority Dropdown
        Text("Priority", style = MaterialTheme.typography.titleMedium)
        PriorityDropdown(priority) {
            priority = it
        }

        Spacer(modifier = Modifier.height(8.dp))
        // Steps Field
        Text("Steps", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = newStepText,
            onValueChange = { newStepText = it },
            placeholder = { Text("Add a step") },
            trailingIcon = {
                IconButton(onClick = {
                    if (newStepText.isNotBlank()) {
                        steps.add(newStepText)
                        newStepText = ""
                    }
                }) {
                    Icon(Icons.Default.Check, contentDescription = "Add Step")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Step List with delete buttons
        steps.forEachIndexed { index, step ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("• $step", modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        steps = steps.toMutableList().apply { removeAt(index) }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Step",
                        tint = Color.Red
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
//                val newTask = Task(
//                    title = title,
//                    description = description,
//                    startDate = startDate.toString(),
//                    endDate = endDate.toString(),
//                    startTime = startTime.toString(),
//                    endTime = endTime.toString(),
//                    priority = priority,
//                    status = TaskStatus.TODO,
//                    steps = steps,
//                    progress = 0
//                )
//                onSaveTask(newTask)
            },
            enabled = title.isNotBlank() && startDate != null && endDate != null,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),

            ) {
            Text("Save Task")
        }


    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun AddTaskScreenPreview() {
    AddTaskScreen()
}

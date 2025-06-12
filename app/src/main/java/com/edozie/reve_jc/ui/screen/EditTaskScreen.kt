package com.edozie.reve_jc.ui.screen

import androidx.hilt.navigation.compose.hiltViewModel
import com.edozie.reve_jc.local.model.Task
import com.edozie.reve_jc.viewmodel.TaskViewModel
import android.os.Build
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.edozie.reve_jc.local.model.Step
import com.edozie.reve_jc.local.model.TaskPriority
import com.edozie.reve_jc.local.model.TaskStatus
import com.edozie.reve_jc.ui.widget.DateTimePickerRow
import com.edozie.reve_jc.ui.widget.PriorityDropdown
import com.edozie.reve_jc.ui.widget.TimePickerRow
import com.edozie.reve_jc.util.CustomBottomNavBar
import com.edozie.reve_jc.util.Routes
import com.edozie.reve_jc.util.Screen
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime

import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.room.util.copy
import kotlinx.coroutines.flow.mapNotNull


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditTaskScreen(
    vm: TaskViewModel = hiltViewModel(),
//    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val currentTask by vm.currentTask.collectAsState()
    val currentSteps by vm.currentSteps.collectAsState()
    val stepsList by currentSteps.collectAsState(initial = emptyList())
//    val stepsList by vm.currentSteps.collectAsState(initial = emptyList())


    var title by remember { mutableStateOf(currentTask?.title!!) }
    var description by remember { mutableStateOf(currentTask?.description!!) }
    var startDate by remember { mutableStateOf(currentTask?.startDate!!) }
    var endDate by remember { mutableStateOf(currentTask?.endDate!!) }
    var startTime by remember { mutableStateOf(currentTask?.startTime!!) }
    var endTime by remember { mutableStateOf(currentTask?.endTime!!) }
    var priority by remember { mutableStateOf(currentTask?.priority!!) }

    var steps by remember(stepsList) {
        mutableStateOf(stepsList.mapNotNull { it?.copy() }.toMutableList())
    }

    var newStepText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(stepsList) {
        steps = stepsList.mapNotNull { it?.copy() }.toMutableList()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        DateTimePickerRow(startDate, endDate, {
            startDate = it
        }, {
            endDate = it
        })

        TimePickerRow(startTime, endTime, {
            startTime = it
        }, {
            endTime = it
        })

        PriorityDropdown(priority) {
            priority = it
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Steps", style = MaterialTheme.typography.titleMedium)
        steps.forEachIndexed { index, step ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = step.description,
                    onValueChange = {
                        steps[index] = step.copy(description = it)
                    },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
//                    viewModel.deleteStep(step)
//                    steps.removeAt(index)
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove Step")
                }
            }
        }

        OutlinedTextField(
            value = newStepText,
            onValueChange = { newStepText = it },
            placeholder = { Text("Add a new step") },
            trailingIcon = {
                IconButton(onClick = {
                    if (newStepText.isNotBlank()) {
//                        steps.add(Step(taskId = task.id, description = newStepText))
                        newStepText = ""
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Step")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val now = LocalDate.now()
                val nowTime = LocalTime.now()

                when {
                    title.isBlank() -> errorMessage = "Title is required"
                    startDate == null || endDate == null -> errorMessage =
                        "Start and end dates are required"

                    startTime == null || endTime == null -> errorMessage =
                        "Start and end times are required"

                    startDate.isBefore(now) ||
                            (startDate == now && startTime.isBefore(nowTime)) ->
                        errorMessage = "Start must be in the future"

                    endDate.isBefore(startDate) ||
                            (startDate == endDate && endTime.isBefore(startTime)) ->
                        errorMessage = "End must be after start"

                    else -> {
                        errorMessage = null

//                        val updatedTask = task.copy(
//                            title = title,
//                            description = description,
//                            startDate = startDate,
//                            endDate = endDate,
//                            startTime = startTime,
//                            endTime = endTime,
//                            priority = priority
//                        )
//
//                        viewModel.updateTask(updatedTask)

//                        steps.forEach { step ->
//                            if (step.id == 0) {
//                                viewModel.addStep(step)
//                            } else {
//                                viewModel.updateStep(step)
//                            }
//                        }
//
//                        onNavigateBack()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Update Task")
        }
    }
}

package com.edozie.reve_jc.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edozie.reve_jc.local.model.Task
import com.edozie.reve_jc.local.model.TaskPriority
import com.edozie.reve_jc.util.Screen

@Composable
fun TaskCard(
    task: Task,
    onEditClick: () -> Unit,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.TaskDetail.createRoute(task.id))
            }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(task.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    task.priority.name, color = when (task.priority) {
                        TaskPriority.HIGH -> Color.Red
                        TaskPriority.MEDIUM -> Color.Yellow
                        TaskPriority.LOW -> Color.Green
                    }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { task.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Start: ${task.startDate}")
            Text("End: ${task.endDate}")

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Task")
                Spacer(Modifier.width(8.dp))
                Text("Edit")
            }
        }
    }
}

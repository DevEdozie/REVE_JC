package com.edozie.reve_jc.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.edozie.reve_jc.R
import com.edozie.reve_jc.local.model.Task
import com.edozie.reve_jc.local.model.TaskStatus
import com.edozie.reve_jc.ui.widget.EmptyTaskPlaceholder
import com.edozie.reve_jc.ui.widget.GreetingSection
import com.edozie.reve_jc.ui.widget.HorizontalPagerTabRow
import com.edozie.reve_jc.ui.widget.TaskCard
import com.edozie.reve_jc.viewmodel.TaskViewModel
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksScreen(
    vm: TaskViewModel = hiltViewModel(),
//    onAddTaskClick: () -> Unit,
//    onEditTaskClick: (Task) -> Unit
) {
    val tasks by vm.allTasks.collectAsState()
    val tabs = listOf("To-do", "In Progress", "Done")
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp) // prevent overlap with FAB
        ) {
            GreetingSection()
            if (tasks.isEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyTaskPlaceholder()
                }

            } else {
                HorizontalPagerTabRow(pagerState = pagerState, tabs = tabs)

                HorizontalPager(state = pagerState) { page ->
                    val filteredTasks = when (page) {
                        0 -> tasks // To-do
                        1 -> tasks.filter { it.progress > 0 && it.status != TaskStatus.DONE }
                        2 -> tasks.filter { it.status == TaskStatus.DONE }
                        else -> tasks
                    }

                    if (filteredTasks.isEmpty()) {
                        EmptyTaskPlaceholder(modifier = Modifier.fillMaxSize())
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            items(filteredTasks) { task ->
                                TaskCard(task = task, onEditClick = { /* Handle edit */ })
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 6.dp,
                pressedElevation = 12.dp
            ),
            shape = CircleShape,
            containerColor = Color(0xFF007AFF),
            contentColor = Color.White,
            onClick = {
                // Handle add task click
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.tasks_ic),
                contentDescription = "New task",
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}
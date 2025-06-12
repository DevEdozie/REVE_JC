package com.edozie.reve_jc.viewmodel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edozie.reve_jc.local.model.Step
import com.edozie.reve_jc.local.model.Task
import com.edozie.reve_jc.local.repo.TaskRepository
import com.edozie.reve_jc.util.AlarmUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val application: Application,
    private val repository: TaskRepository
) : AndroidViewModel(application) {
    val allTasks =
        repository.getAllTasks().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    @RequiresApi(Build.VERSION_CODES.O)
    val todayTasks = repository.getTasksForDate(LocalDate.now())
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _currentTask = MutableStateFlow<Task?>(null)
    val currentTask: StateFlow<Task?> = _currentTask

    private var _currentSteps = MutableStateFlow<Flow<List<Step?>>>(emptyFlow())
    val currentSteps: StateFlow<Flow<List<Step?>>> = _currentSteps

    fun loadTask(id: Int) = viewModelScope.launch {
        _currentTask.value = repository.getTaskById(id)
    }

    fun loadSteps(id: Int) = viewModelScope.launch {
        _currentSteps.value = repository.getSteps(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTask(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
        AlarmUtil.setAlarmsForTask(application, task)
    }

    // ADD TASK WITH STEPS
    @RequiresApi(Build.VERSION_CODES.O)
    fun addTaskWIthSteps(task: Task, steps: List<String>) =
        viewModelScope.launch {
            val taskId = repository.insertTask(task).toInt()
            steps.forEach { taskDescription ->
                repository.insertStep(Step(taskId = taskId, description = taskDescription))
            }
            AlarmUtil.setAlarmsForTask(application, task.copy(id = taskId))
        }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun updateTask(task: Task) = viewModelScope.launch {
//        repository.updateTask(task)
//        AlarmUtil.cancelAlarmsForTask(application, task.id)
//        AlarmUtil.setAlarmsForTask(application, task)
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTaskWithAlarm(task: Task) = viewModelScope.launch {
        // Called when date/time changed: cancel old alarms, update DB, then set new alarms
        repository.updateTask(task)
        _currentTask.value = task // 🔥 Trigger StateFlow update for recomposition
        AlarmUtil.cancelAlarmsForTask(application, task.id)
        AlarmUtil.setAlarmsForTask(application, task)
    }

    fun updateTaskWithoutAlarm(task: Task) = viewModelScope.launch {
        // Called when only progress/status changes: just update DB, do not touch alarms
        repository.updateTask(task)
        _currentTask.value = task // 🔥 Trigger StateFlow update for recomposition
    }

    fun cancelAlarmsForTaskOnly(taskId: Int) = viewModelScope.launch {
        AlarmUtil.cancelAlarmsForTask(application, taskId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setAlarmsForTaskOnly(task: Task) = viewModelScope.launch {
        AlarmUtil.setAlarmsForTask(application, task)
    }


    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
        AlarmUtil.cancelAlarmsForTask(application, task.id)
    }

    fun addStep(step: Step) = viewModelScope.launch {
        repository.insertStep(step)
    }

    fun markStepDone(step: Step) = viewModelScope.launch {
        repository.updateStep(step.copy(isDone = true))
    }

    fun updateStep(step: Step) = viewModelScope.launch {
        repository.updateStep(step)
    }

    fun deleteStep(step: Step) = viewModelScope.launch {
        repository.deleteStep(step)
    }



//    fun stepsForTask(taskId: Int): Flow<List<Step>> = repository.getSteps(taskId)


}

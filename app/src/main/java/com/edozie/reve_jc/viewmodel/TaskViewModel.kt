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

    fun loadTask(id: Int) = viewModelScope.launch {
        _currentTask.value = repository.getTaskById(id)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addTask(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
        AlarmUtil.setAlarmsForTask(application, task)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTask(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
        AlarmUtil.cancelAlarmsForTask(application, task.id)
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

    fun stepsForTask(taskId: Int): Flow<List<Step>> = repository.getSteps(taskId)
}

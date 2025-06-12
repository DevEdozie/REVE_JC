package com.edozie.reve_jc.local.repo

import com.edozie.reve_jc.local.dao.TaskDao
import com.edozie.reve_jc.local.model.Step
import com.edozie.reve_jc.local.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    fun getTasksForDate(date: LocalDate): Flow<List<Task>> = taskDao.getTasksForDate(date)
    suspend fun getTaskById(id: Int): Task = taskDao.getTaskById(id)
    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    fun getSteps(taskId: Int): Flow<List<Step>> = taskDao.getStepsForTask(taskId)
    suspend fun insertStep(step: Step) = taskDao.insertStep(step)
    suspend fun updateStep(step: Step) = taskDao.updateStep(step)
    suspend fun deleteStep(step: Step) = taskDao.deleteStep(step)
}
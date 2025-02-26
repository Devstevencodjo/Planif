package com.example.plannif.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plannif.dao.TaskDao
import com.example.plannif.entity.Task
import com.example.plannif.entity.TaskPriority
import com.example.plannif.api.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskDao: TaskDao,
    private val apiService: ApiService
) : ViewModel() {

    val tasks: Flow<List<Task>> = taskDao.getAllTasks()

    fun addTask(title: String, priority: TaskPriority = TaskPriority.MEDIUM, reminderTime: Long? = null) {
        viewModelScope.launch {
            try {
                taskDao.insertTask(Task(title = title, priority = priority, reminderTime = reminderTime))
            } catch (e: Exception) {
                Log.e("TASK", "Erreur lors de l'ajout : ${e.message}")
            }
        }
    }


    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task)
        }
    }


    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }

    fun syncTasks() {
        viewModelScope.launch {
            try {
                val remoteTasks = apiService.getTasks()
                remoteTasks.forEach { task ->
                    taskDao.insertTask(Task(title = task["title"].toString()))
                }
                Log.d("SYNC", "Tâches synchronisées : $remoteTasks")
            } catch (e: Exception) {
                Log.e("SYNC", "Erreur lors de la synchronisation : ${e.message}")
            }
        }
    }
}
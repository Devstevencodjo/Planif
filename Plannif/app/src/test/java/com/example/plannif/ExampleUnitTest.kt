package com.example.plannif

import com.example.plannif.dao.TaskDao
import com.example.plannif.entity.Task
import com.example.plannif.viewmodel.TaskViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlinx.coroutines.flow.first


class ExampleUnitTest {
    private val taskDao: TaskDao = mock()
    private val viewModel = TaskViewModel(taskDao)

    @Test
    fun testAddTask() = runTest {
        // Teste l'ajout d'une tâche
        viewModel.addTask("Test Task")
        verify(taskDao).insertTask(Task(title = "Test Task"))
    }

    @Test
    fun testGetAllTasks() = runTest {
        // Simule une liste de tâches
        val fakeTasks = listOf(Task(title = "Task 1"), Task(title = "Task 2"))
        whenever(taskDao.getAllTasks()).thenReturn(flowOf(fakeTasks))

        // Vérifie que le ViewModel retourne les données simulées
        val tasks = viewModel.tasks.first()
        assertEquals(2, tasks.size)
    }
}
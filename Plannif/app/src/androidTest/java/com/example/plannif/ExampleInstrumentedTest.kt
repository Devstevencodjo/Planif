package com.example.plannif

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.plannif.database.AppDatabase
import com.example.plannif.dao.TaskDao
import com.example.plannif.entity.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var database: AppDatabase
    private lateinit var taskDao: TaskDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        taskDao = database.taskDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrieveTasks() = runBlocking {
        // Ajoute une tâche
        taskDao.insertTask(Task(title = "Test Task"))
        val tasks = taskDao.getAllTasks().first()

        // Vérifie qu'elle est bien récupérée
        assertEquals(1, tasks.size)
        assertEquals("Test Task", tasks[0].title)
    }
}
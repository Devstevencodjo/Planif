package com.example.plannif

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plannif.api.ApiService
import com.example.plannif.api.ktorClient
import com.example.plannif.database.AppDatabase
import com.example.plannif.ui.theme.PlannifTheme
import com.example.plannif.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlannifTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    val db = remember { AppDatabase.getDatabase(context) }
                    val apiService = remember { ApiService(ktorClient) }
                    val viewModel: TaskViewModel = viewModel {
                        TaskViewModel(db.taskDao(), apiService)
                    }

                    Greeting(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(viewModel: TaskViewModel, modifier: Modifier = Modifier) {
    var taskName by remember { mutableStateOf("") }
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = taskName,
            onValueChange = { taskName = it },
            label = { Text("Nom de la tâche") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (taskName.isNotBlank()) {
                    viewModel.addTask(taskName)
                    viewModel.syncTasks()
                    taskName = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Créer")
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(tasks) { task ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = task.title)
                            Text(
                                text = "Créée le : ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(task.createdAt))}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { viewModel.deleteTask(task) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Supprimer"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PlannifTheme {
        val context = LocalContext.current
        val db = AppDatabase.getDatabase(context)
        val apiService = ApiService(ktorClient)
        Greeting(
            viewModel = TaskViewModel(db.taskDao(), apiService),
            modifier = Modifier.padding(16.dp)
        )
    }
}
package com.example.plannif.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


enum class TaskPriority {
    LOW, MEDIUM, HIGH
}

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val createdAt: Long = System.currentTimeMillis(),
    val reminderTime: Long? = null
)
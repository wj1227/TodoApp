package com.example.todoapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.data.models.Priority

@Entity(tableName = "todo")
data class ToDoData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val priority: Priority,
    val description: String
)
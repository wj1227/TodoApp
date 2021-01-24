package com.example.todoapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.TodoDatabase
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application): AndroidViewModel(application) {

    private val todoDao = TodoDatabase.getDatabase(
        application
    ).todoDao()
    private val repository: TodoRepository

    private val getAllData: LiveData<List<ToDoData>>

    init {
        repository = TodoRepository(todoDao)
        getAllData = repository.getAllData
    }

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(todoData = toDoData)
        }
    }
}
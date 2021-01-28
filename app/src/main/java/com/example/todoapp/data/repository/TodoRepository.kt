package com.example.todoapp.data.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.data.ToDoDao
import com.example.todoapp.data.models.ToDoData

class TodoRepository(private val todoDao: ToDoDao) {

    val getAllData: LiveData<List<ToDoData>> = todoDao.getAllData()
    val sortByHightPriority: LiveData<List<ToDoData>> = todoDao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<ToDoData>> = todoDao.sortByLowPriority()

    suspend fun insertData(todoData: ToDoData) {
        todoDao.insertData(todoData)
    }

    suspend fun updateData(todoData: ToDoData) {
        todoDao.updateData(todoData)
    }

    suspend fun deleteData(todoData: ToDoData) {
        todoDao.deleteData(todoData)
    }

    suspend fun deleteAll() {
        todoDao.deleteAll()
    }

    fun searchTodo(query: String): LiveData<List<ToDoData>> {
        return todoDao.searchTodo(query)
    }
}
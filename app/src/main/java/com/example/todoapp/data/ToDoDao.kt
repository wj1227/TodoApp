package com.example.todoapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.*
import com.example.todoapp.data.models.ToDoData

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo ORDER BY id ASC")
    fun getAllData(): LiveData<List<ToDoData>>

    @Insert(onConflict = IGNORE)
    suspend fun insertData(todoData: ToDoData)

    @Update
    suspend fun updateData(todoData: ToDoData)

    @Delete
    suspend fun deleteData(todoData: ToDoData)

    @Query("DELETE FROM todo")
    suspend fun deleteAll()
}
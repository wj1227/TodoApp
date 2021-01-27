package com.example.todoapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.data.models.Priority
import com.example.todoapp.fragments.list.Identifiable
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "todo")
@Parcelize
data class ToDoData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val priority: Priority,
    val description: String
) : Parcelable, Identifiable {
    override val itentifier: Any
        get() = id
}
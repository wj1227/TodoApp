package com.example.todoapp.fragments

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.utils.HIGH_PRIORITY
import com.example.todoapp.utils.LOW_PRIORITY
import com.example.todoapp.utils.MEDIUM_PRIORITY

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(true)

    val listener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (position) {
                0 -> (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.red))
                1 -> (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.yellow))
                2 -> (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green))
            }
        }
    }

    fun verityDataFromUser(title: String, description: String): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }

    fun parsePriority(priority: String): Priority {
        return when (priority) {
            HIGH_PRIORITY -> Priority.HIGH
            MEDIUM_PRIORITY -> Priority.MEDIUM
            LOW_PRIORITY -> Priority.LOW
            else -> throw IllegalStateException("Not Found Priority")
        }
    }

    fun parsePriorityToInt(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }

    fun checkDatabase(todoData: List<ToDoData>) {
        emptyDatabase.value = todoData.isEmpty()
    }

}
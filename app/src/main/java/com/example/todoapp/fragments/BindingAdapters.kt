package com.example.todoapp.fragments

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.fragments.list.ListFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapters {

    companion object {
        @BindingAdapter("bindNavigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
             view.setOnClickListener {
                 if (navigate) {
                     view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                 }
             }
        }

        @BindingAdapter("bindEmptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                else -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("bindPriorityToInt")
        @JvmStatic
        fun parsePriorityToInt(view: Spinner, priority: Priority) {
            return when (priority) {
                Priority.HIGH -> view.setSelection(0)
                Priority.MEDIUM -> view.setSelection(1)
                Priority.LOW -> view.setSelection(2)
            }
        }

        @BindingAdapter("bindBackGroundCardView")
        @JvmStatic
        fun parsePriorityColor(cv: CardView, priority: Priority) {
            when (priority) {
                Priority.HIGH -> cv.setCardBackgroundColor(ContextCompat.getColor(cv.context, R.color.red))
                Priority.MEDIUM -> cv.setCardBackgroundColor(ContextCompat.getColor(cv.context, R.color.yellow))
                Priority.LOW -> cv.setCardBackgroundColor(ContextCompat.getColor(cv.context, R.color.green))
            }
        }

//        @BindingAdapter("bindSendToUpdateFragment")
//        @JvmStatic
//        fun sendDataToUpdateFragment(view: ConstraintLayout, item: ToDoData) {
//            view.setOnClickListener {
//                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(item)
//                view.findNavController().navigate(action)
//            }
//        }
    }
}
package com.example.todoapp.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData

typealias OnRecyclerViewItemClick = (ToDoData) -> Unit

class ListAdapter(
    private val onItemClick: OnRecyclerViewItemClick? = null
) : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var dataList = emptyList<ToDoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)).also { holder ->
            if (onItemClick == null) {
                return@also
            } else {
                holder.itemView.setOnClickListener {
                    val currentItem = dataList.getOrNull(holder.adapterPosition) ?: return@setOnClickListener
                    onItemClick.invoke(currentItem)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val todoTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val todoDescription = itemView.findViewById<TextView>(R.id.tv_description)
        private val todoPriority = itemView.findViewById<CardView>(R.id.cv_priority)

        fun bind(todoData: ToDoData) {
            todoTitle.text = todoData.title
            todoDescription.text = todoData.description
            when (todoData.priority) {
                Priority.HIGH -> todoPriority.setCardBackgroundColor(ContextCompat.getColor(todoDescription.context, R.color.red))
                Priority.MEDIUM -> todoPriority.setCardBackgroundColor(ContextCompat.getColor(todoDescription.context, R.color.yellow))
                Priority.LOW -> todoPriority.setCardBackgroundColor(ContextCompat.getColor(todoDescription.context, R.color.green))
            }
        }
    }

    fun addItems(todoData: List<ToDoData>) {
        this.dataList = todoData
        notifyDataSetChanged()
    }
}
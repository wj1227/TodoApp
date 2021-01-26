package com.example.todoapp.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.RowLayoutBinding

typealias OnRecyclerViewItemClick = (ToDoData) -> Unit

class ListAdapter(
    private val onItemClick: OnRecyclerViewItemClick? = null
) : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var dataList = emptyList<ToDoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent).also {
            if (onItemClick == null) {
                return@also
            } else {
                it.itemView.setOnClickListener { _ ->
                    val currentItem = dataList.getOrNull(it.adapterPosition) ?: return@setOnClickListener
                    onItemClick.invoke(currentItem)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size

    class MyViewHolder(private val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todoData: ToDoData) {
            binding.item = todoData
            binding.executePendingBindings()
        }

        companion object Factory {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater, parent, false)

                return MyViewHolder(binding)
            }
        }
    }

    fun addItems(todoData: List<ToDoData>) {
        this.dataList = todoData
        notifyDataSetChanged()
    }
}
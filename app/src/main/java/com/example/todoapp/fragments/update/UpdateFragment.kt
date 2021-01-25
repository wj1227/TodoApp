package com.example.todoapp.fragments.update

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.viewmodel.TodoViewModel
import com.example.todoapp.fragments.SharedViewModel
import com.example.todoapp.utils.hideKeyboard
import com.example.todoapp.utils.showToast
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewmodel: SharedViewModel by viewModels()
    private val viewModel: TodoViewModel by viewModels()

    private lateinit var title: EditText
    private lateinit var priority: Spinner
    private lateinit var description: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_update, container, false)

        title = view.findViewById(R.id.et_title)
        priority = view.findViewById(R.id.sn_priorities)
        description = view.findViewById(R.id.et_description)
        priority.onItemSelectedListener = sharedViewmodel.listener

        setTodoItem()

        setHasOptionsMenu(true)

        return view
    }

    private fun setTodoItem() {
        title.setText(args.todoItem.title)
        description.setText(args.todoItem.description)
        priority.setSelection(sharedViewmodel.parsePriorityToInt(args.todoItem.priority))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateTodo()
            R.id.menu_delete -> confirmDeleteItem()
            else -> throw IllegalStateException("Not Found Menu Item")
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updateTodo() {
        val title = title.text.toString()
        val description = description.text.toString()
        val priority = priority.selectedItem.toString()

        val validation = sharedViewmodel.verityDataFromUser(title, description)

        if (validation) {
            val item = ToDoData(
                id = args.todoItem.id,
                title = title,
                priority = sharedViewmodel.parsePriority(priority),
                description = description
            )

            viewModel.updateData(item)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            this.context?.showToast("Successfully updated!")
        } else {
            this.context?.showToast("Please all fields!")
        }
    }

    private fun confirmDeleteItem() {
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Delete '${args.todoItem.title}'?")
        alertDialog.setMessage("Are you sure you want to remove '${args.todoItem.title}'?")
        alertDialog.setPositiveButton("YES") { _, _ ->
            deleteData()
        }
        alertDialog.setNegativeButton("NO") { _, _ -> }

        alertDialog.show()
    }

    private fun deleteData() {
        viewModel.deleteData(args.todoItem)
        findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        this.context?.showToast("Successfully deleted!")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        view?.hideKeyboard()
    }
}
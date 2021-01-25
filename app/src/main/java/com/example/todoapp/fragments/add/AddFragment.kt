package com.example.todoapp.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.viewmodel.TodoViewModel
import com.example.todoapp.fragments.SharedViewModel
import com.example.todoapp.utils.hideKeyboard
import com.example.todoapp.utils.showToast

class AddFragment : Fragment() {

    private val viewModel: TodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_add, container, false)

        title = view.findViewById(R.id.et_title)
        description = view.findViewById(R.id.et_description)
        spinner = view.findViewById(R.id.sn_priorities)

        spinner.onItemSelectedListener = sharedViewModel.listener

        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataTodo()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataTodo() {
        val todoTitle = title.text.toString()
        val todoPriority = spinner.selectedItem.toString()
        val todoDescription = description.text.toString()

        val validation = sharedViewModel.verityDataFromUser(todoTitle, todoDescription)

        if (validation) {
            val todoData = ToDoData(
                id = 0,
                title = todoTitle,
                priority = sharedViewModel.parsePriority(todoPriority),
                description = todoDescription
            )

            viewModel.insertData(todoData)
            this.context?.showToast("Successfully added!")
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            this.context?.showToast("Please all fields!")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        view?.hideKeyboard()
    }

}
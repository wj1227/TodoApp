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
import com.example.todoapp.databinding.FragmentAddBinding
import com.example.todoapp.fragments.SharedViewModel
import com.example.todoapp.utils.hideKeyboard
import com.example.todoapp.utils.showToast

class AddFragment : Fragment() {

    private val viewModel: TodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.snPriorities.onItemSelectedListener = sharedViewModel.listener

        setHasOptionsMenu(true)

        return binding.root
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
        val todoTitle = binding.etTitle.text.toString()
        val todoPriority = binding.snPriorities.selectedItem.toString()
        val todoDescription = binding.etDescription.text.toString()

        val validation = sharedViewModel.verityDataFromUser(todoTitle, todoDescription)

        if (validation) {
            val todoData = ToDoData(
                id = 0,
                title = todoTitle,
                priority = sharedViewModel.parsePriority(todoPriority),
                description = todoDescription
            )

            viewModel.insertData(todoData)
            binding.root.context.showToast("Successfully added!")
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            binding.root.context.showToast("Please all fields!")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.root.hideKeyboard()
        _binding = null
    }

}
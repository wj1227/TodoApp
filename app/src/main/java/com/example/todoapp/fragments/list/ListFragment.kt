package com.example.todoapp.fragments.list

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.viewmodel.TodoViewModel
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.fragments.SharedViewModel
import com.example.todoapp.utils.hideKeyboard
import com.example.todoapp.utils.showToast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListFragment : Fragment() {

    private val viewModel: TodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        adapter = ListAdapter { todoData ->
            val aciton = ListFragmentDirections.actionListFragmentToUpdateFragment(todoData)
            findNavController().navigate(aciton)
        }

        binding.rvList.adapter = adapter

        initViewModelObserving()

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            getAllData.observe(viewLifecycleOwner, Observer { data ->
                sharedViewModel.checkDatabase(data)
                adapter.addItems(data)
            })
        }
    }

    private fun confirmDeleteItems() {
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Delete Everything?")
        alertDialog.setMessage("Are you sure you want to remove Everything?")
        alertDialog.setPositiveButton("YES") { _, _ ->
            if (adapter.itemCount == 0) {
                binding.root.context.showToast("No data")
            } else {
                viewModel.deleteAll()
            }
        }
        alertDialog.setNegativeButton("NO") { _, _ -> }

        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmDeleteItems()
            //else -> IllegalStateException("Not Found Menu Item Id")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.root.hideKeyboard()
        _binding = null
    }

}
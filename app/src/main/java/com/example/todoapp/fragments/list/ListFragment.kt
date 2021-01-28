package com.example.todoapp.fragments.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.viewmodel.TodoViewModel
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.fragments.SharedViewModel
import com.example.todoapp.utils.hideKeyboard
import com.example.todoapp.utils.showToast
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val viewModel: TodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var adapter: TodoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        adapter = TodoListAdapter { todoData ->
            val aciton = ListFragmentDirections.actionListFragmentToUpdateFragment(todoData)
            findNavController().navigate(aciton)
        }

        binding.rvList.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
//        binding.rvList.itemAnimator = LandingAnimator().apply {
//            addDuration = 300
//        }
        binding.rvList.adapter = adapter
        swipeToDelete(binding.rvList)

        initViewModelObserving()

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            getAllData.observe(viewLifecycleOwner, Observer { data ->
                sharedViewModel.checkDatabase(data)
                //adapter.addItems(data)
                adapter.submitList(data)
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

    private fun swipeToDelete(rv: RecyclerView) {
        val swipeCallback = object : SwipeDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteData(item)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                binding.root.context.showToast("Successfully Removed '${item.title}'")

                restoreDeleteData(viewHolder.itemView, item)
            }
        }

        val itemHelper = ItemTouchHelper(swipeCallback)
        itemHelper.attachToRecyclerView(rv)
    }

    private fun restoreDeleteData(view: View, deletedItem: ToDoData) {
        val snackbar = Snackbar.make(view, "Deleted '${deletedItem.title}'", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            viewModel.insertData(deletedItem)
            //adapter.notifyItemChanged(position)
        }.show()
    }

    private fun searchQuery(query: String) {
        viewModel.searchTodo(query).observe(this, Observer { list ->
            list?.let {
                println("?: $it")
                adapter.submitList(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> {
                confirmDeleteItems()
            }
            R.id.menu_priority_high -> {
                viewModel.sortByHighPriority.observe(this, Observer {
                    adapter.submitList(it)
                })
            }
            R.id.menu_priority_low -> {
                viewModel.sortByLowPriority.observe(this, Observer {
                    adapter.submitList(it)
                })
            }
            //else -> IllegalStateException("Not Found Menu Item Id")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchQuery(query)
        }

        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchQuery(query)
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.root.hideKeyboard()
        _binding = null
    }

}
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
import com.example.todoapp.fragments.SharedViewModel
import com.example.todoapp.utils.hideKeyboard
import com.example.todoapp.utils.showToast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListFragment : Fragment() {

    private val viewModel: TodoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private lateinit var floatingButton: FloatingActionButton
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListAdapter
    private lateinit var ivEmpty: ImageView
    private lateinit var tvEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)

        floatingButton = view.findViewById(R.id.btn_floating)
        constraintLayout = view.findViewById(R.id.ctl_list)
        recyclerView = view.findViewById(R.id.rv_list)
        ivEmpty = view.findViewById(R.id.iv_empty_data)
        tvEmpty = view.findViewById(R.id.tv_empty)

        adapter = ListAdapter { todoData ->
            val aciton = ListFragmentDirections.actionListFragmentToUpdateFragment(todoData)
            findNavController().navigate(aciton)
        }

        recyclerView.adapter = adapter

        initClickListener()
        initViewModelObserving()

        setHasOptionsMenu(true)

        return view
    }

    private fun initClickListener() {
        floatingButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
    }

    private fun initViewModelObserving() {
        with(viewModel) {
            getAllData.observe(viewLifecycleOwner, Observer { data ->
                sharedViewModel.checkDatabase(data)
                adapter.addItems(data)
            })
        }
        with(sharedViewModel) {
            emptyDatabase.observe(viewLifecycleOwner, Observer {
                showEmptyView(it)
            })
        }
    }

    private fun showEmptyView(empty: Boolean) {
        if (empty) {
            ivEmpty.visibility = View.VISIBLE
            tvEmpty.visibility = View.VISIBLE
        } else {
            ivEmpty.visibility = View.INVISIBLE
            tvEmpty.visibility = View.INVISIBLE
        }
    }

    private fun confirmDeleteItems() {
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Delete Everything?")
        alertDialog.setMessage("Are you sure you want to remove Everything?")
        alertDialog.setPositiveButton("YES") { _, _ ->
            if (adapter.itemCount == 0) {
                 this.context?.showToast("No data")
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
            else -> IllegalStateException("Not Found Menu Item Id")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        view?.hideKeyboard()
    }

}
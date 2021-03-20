package com.reemhazzaa.daftar.tasks.fragments.tasksList

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.reemhazzaa.daftar.MainActivity
import com.reemhazzaa.daftar.R
import com.reemhazzaa.daftar.databinding.FragmentTasksListBinding
import com.reemhazzaa.daftar.tasks.data.models.Task
import com.reemhazzaa.daftar.tasks.data.viewModel.SharedViewModel
import com.reemhazzaa.daftar.tasks.data.viewModel.TaskViewModel
import com.reemhazzaa.daftar.tasks.fragments.tasksList.adapters.TasksAdapter
import com.reemhazzaa.daftar.tasks.utils.SwipeToDelete
import com.reemhazzaa.daftar.tasks.utils.hideVirtualKeyboard
import jp.wasabeef.recyclerview.animators.LandingAnimator

class TasksListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentTasksListBinding? = null
    private val binding get() = _binding!!

    private val tasksViewModel: TaskViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val listAdapter: TasksAdapter by lazy { TasksAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTasksListBinding.inflate(layoutInflater, container, false)

        val toolbar = binding.titleTV
        (this.activity as MainActivity).setSupportActionBar(toolbar)
        (this.activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        setHasOptionsMenu(true)

        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        binding.tasksRV.apply {
            adapter = listAdapter
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            swipeToDelete(this)
            itemAnimator = LandingAnimator().apply {
                addDuration = 300
            }
        }

        tasksViewModel.getAllData.observe(viewLifecycleOwner, Observer { tasksList ->
            sharedViewModel.checkIfDatabaseEmpty(tasksList)
            listAdapter.setList(tasksList)
        })

        return binding.root
    }

    private fun swipeToDelete(rv: RecyclerView) {
        val swipeToDelete = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = listAdapter.getList()[viewHolder.adapterPosition]
                tasksViewModel.deleteItem(itemToDelete)
                listAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                // Restore
                restoreDeletedItem(viewHolder.itemView, itemToDelete, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(rv)
    }

    private fun restoreDeletedItem(view: View, deletedItem: Task, position: Int) {
        val snackbar = Snackbar.make(
            view, "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo") {
            tasksViewModel.insertData(deletedItem)
        }
        snackbar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_list_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmDeleteAll()
            R.id.menu_priority_high -> tasksViewModel.sortByHighPriority.observe(
                this,
                { listAdapter.setList(it) })
            R.id.menu_priority_low -> tasksViewModel.sortByLowPriority.observe(
                this,
                { listAdapter.setList(it) })
            R.id.menu_linear -> binding.tasksRV.apply {
                layoutManager = LinearLayoutManager(requireContext())
            }
            R.id.menu_grid -> binding.tasksRV.apply {
                layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete All Tasks")
        builder.setMessage("Are you sure you want to delete all tasks?")
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            tasksViewModel.deleteAll()
            onOperationSuccess("All tasks successfully deleted")
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
        }
        builder.create().show()
    }

    private fun onOperationSuccess(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        hideVirtualKeyboard(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchTasksDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchTasksDatabase(query)
        }
        return true
    }

    private fun searchTasksDatabase(query: String) {
        var searchQuery = query
        searchQuery = "%$searchQuery%"

        tasksViewModel.searchDatabase(searchQuery).observe(this, { list ->
            list.let {
                listAdapter.setList(it)
            }
        })
    }

}
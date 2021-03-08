package com.reemhazzaa.daftar.tasks.fragments.tasksList

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.reemhazzaa.daftar.MainActivity
import com.reemhazzaa.daftar.R
import com.reemhazzaa.daftar.tasks.data.viewModel.SharedViewModel
import com.reemhazzaa.daftar.tasks.data.viewModel.TaskViewModel
import com.reemhazzaa.daftar.databinding.FragmentTasksListBinding
import com.reemhazzaa.daftar.tasks.utils.hideVirtualKeyboard

class TasksListFragment : Fragment() {

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

        binding.tasksRV.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        tasksViewModel.getAllData.observe(viewLifecycleOwner, Observer { tasksList ->
            sharedViewModel.checkIfDatabaseEmpty(tasksList)
            listAdapter.setList(tasksList)
        })
        binding.addTaskButton.setOnClickListener {
            findNavController().navigate(R.id.action_tasksListFragment_to_addTaskFragment)
        }

        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer { isDatabaseEmpty ->
            showNoData(isDatabaseEmpty)
        })

        return binding.root
    }

    private fun showNoData(databaseEmpty: Boolean) {
        if (databaseEmpty){
            binding.noDataIV.visibility = View.VISIBLE
            binding.errorTV.visibility = View.VISIBLE
        } else {
            binding.noDataIV.visibility = View.INVISIBLE
            binding.errorTV.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_search -> {}
            R.id.menu_delete_all -> confirmDeleteAll()
            R.id.menu_sortBy -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete All Tasks")
        builder.setMessage("Are you sure you want to delete all tasks?")
        builder.setPositiveButton(getString(R.string.yes)) { _,_ ->
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

}
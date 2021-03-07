package com.reemhazzaa.daftar.tasks

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.reemhazzaa.daftar.R
import com.reemhazzaa.daftar.data.tasks.models.Task
import com.reemhazzaa.daftar.data.tasks.viewModel.TaskViewModel
import com.reemhazzaa.daftar.databinding.FragmentUpdateTaskBinding

class UpdateTaskFragment : Fragment() {
    private var _binding: FragmentUpdateTaskBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateTaskFragmentArgs>()
    private val tasksViewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateTaskBinding.inflate(layoutInflater, container, false)

        updateUI(args.currentItem)

        return binding.root
    }

    private fun updateUI(currentItem: Task) {
        binding.apply {
            titleET.setText(currentItem.title)
            taskDescriptionET.setText(currentItem.description)
            spinner.setSelection(parsePriorityToInt(currentItem.priority))
            spinner.onItemSelectedListener = spinnerListener(requireContext())
            updateTaskButton.setOnClickListener {
                attemptUpdateItemInDb()
            }
            deleteTaskButton.setOnClickListener {
                confirmDeleteItem()
            }
        }
    }

    private fun confirmDeleteItem() {
        val itemTitle = args.currentItem.title
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete '$itemTitle'")
        builder.setMessage("Are you sure you want to delete '$itemTitle'?")
        builder.setPositiveButton(getString(R.string.yes)) { _,_ ->
            tasksViewModel.deleteItem(args.currentItem)
            onOperationSuccess("Successfully deleted '$itemTitle'")
        }
        builder.setNegativeButton("No") { _,_ ->
        }
        builder.create().show()
    }

    private fun attemptUpdateItemInDb() {
        val title = binding.titleET.text.toString().trim()
        val priority = binding.spinner.selectedItemPosition
        val reminder = binding.taskReminderET.text.toString().trim()
        val event = binding.eventET.text.toString().trim()
        val description = binding.taskDescriptionET.text.toString().trim()

        // INIT ERROR(s)
        binding.apply {
            null.let {
                titleET.error = it
                taskDescriptionET.error = it
            }
        }

        // VALIDATE
        var focusView: View? = null
        var cancel = false
        if (description.isBlank()) {
            binding.taskDescriptionET.setError(
                requireContext().getString(R.string.cant_be_empty),
                null
            )
            focusView = binding.taskDescriptionET
            cancel = true
        }
        if (title.isBlank()) {
            binding.titleET.setError(requireContext().getString(R.string.cant_be_empty), null)
            focusView = binding.titleET
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            updateTaskInDb(title, description, reminder, event, priority)
        }
    }

    private fun updateTaskInDb(
        title: String,
        description: String,
        reminder: String,
        event: String,
        priority: Int
    ) {
        val task = Task(
            id = args.currentItem.id,
            title = title,
            description = description,
            priority = parsePriorityIntToString(priority),
            event = event,
            alarm = reminder
        )
        tasksViewModel.updateData(task)
        onOperationSuccess(getString(R.string.success_update))
    }

    private fun onOperationSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        hideVirtualKeyboard(requireActivity())
        findNavController().navigate(R.id.action_updateTaskFragment_to_tasksListFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
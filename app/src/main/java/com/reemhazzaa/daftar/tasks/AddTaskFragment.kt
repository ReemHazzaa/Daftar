package com.reemhazzaa.daftar.tasks

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.reemhazzaa.daftar.R
import com.reemhazzaa.daftar.data.tasks.models.Priority
import com.reemhazzaa.daftar.data.tasks.models.Task
import com.reemhazzaa.daftar.data.tasks.viewModel.TaskViewModel
import com.reemhazzaa.daftar.databinding.FragmentAddTaskBinding

class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private val tasksViewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTaskBinding.inflate(layoutInflater, container, false)

        // change spinner item look
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            requireContext().resources.getStringArray(R.array.priorities)
        )
        binding.spinner.apply {
            adapter = arrayAdapter
            onItemSelectedListener = spinnerListener(requireContext())
        }

        binding.addTaskButton.setOnClickListener {
            attemptInsertTaskToDB()
        }

        return binding.root
    }

    private fun attemptInsertTaskToDB() {
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
            insertTaskToDb(title, description, reminder, event, priority)
        }
    }

    private fun insertTaskToDb(
        title: String,
        description: String,
        reminder: String,
        event: String,
        priority: Int
    ) {
        val newTask = Task(
            id = 0,
            title = title,
            priority = parsePriorityIntToString(priority),
            description = description,
            alarm = reminder,
            event = event
        )
        tasksViewModel.insertData(newTask)
        Toast.makeText(requireContext(), getString(R.string.success_add), Toast.LENGTH_SHORT).show()
        hideVirtualKeyboard(requireActivity())
        findNavController().navigate(R.id.action_addTaskFragment_to_tasksListFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.reemhazzaa.daftar.tasks.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.reemhazzaa.daftar.R
import com.reemhazzaa.daftar.databinding.FragmentUpdateTaskBinding
import com.reemhazzaa.daftar.tasks.data.models.Task
import com.reemhazzaa.daftar.tasks.data.viewModel.SharedViewModel
import com.reemhazzaa.daftar.tasks.data.viewModel.TaskViewModel
import com.reemhazzaa.daftar.tasks.parsePriorityIntToString
import com.reemhazzaa.daftar.tasks.utils.hideVirtualKeyboard
import java.text.SimpleDateFormat
import java.util.*

class UpdateTaskFragment : Fragment() {
    private var _binding: FragmentUpdateTaskBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateTaskFragmentArgs>()
    private val tasksViewModel: TaskViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var myCalendar: Calendar

    private var globalYear = 2021
    private var globalMonth = 1
    private var globalDay = 1
    private var globalHour = 1
    private var globalMinute = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateTaskBinding.inflate(layoutInflater, container, false)
        binding.args = args
        myCalendar = Calendar.getInstance()

        // change spinner item look
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            requireContext().resources.getStringArray(R.array.priorities)
        )
        binding.apply {
            spinner.apply {
                adapter = arrayAdapter
                onItemSelectedListener = sharedViewModel.listener
            }
            setTimeBT.setOnClickListener {
                openTimePicker()
            }
            setDateBT.setOnClickListener {
                openDatePicker()
            }
            updateTaskButton.setOnClickListener {
                attemptUpdateItemInDb()
            }
            deleteTaskButton.setOnClickListener {
                confirmDeleteItem()
            }
            alarmSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    attemptUpdateItemInDb()
                } else {
                    sharedViewModel.cancelAlarm(requireContext())
                }
            }

        }
        return binding.root
    }

    private fun openTimePicker() {
        val mCurrentTime = Calendar.getInstance()
        val hour = mCurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mCurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog = TimePickerDialog(
            requireContext(),
            { timePicker, selectedHour, selectedMinute ->
                val reminderTime = "$selectedHour:$selectedMinute"
                binding.timeTV.text = reminderTime
                globalHour = selectedHour
                globalMinute = selectedMinute
            }, hour, minute, false
        ) // 12 hour time

        mTimePicker.show()
    }

    private fun openDatePicker() {
        val date: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateEditTextWithDate()
                globalYear = year
                globalMonth = monthOfYear
                globalDay = dayOfMonth
            }
        DatePickerDialog(
            requireContext(), date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun updateEditTextWithDate() {
        val myFormat = "dd-MM-yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        val formattedDate = sdf.format(myCalendar.time)

        binding.dateTV.text = formattedDate
    }

    private fun confirmDeleteItem() {
        val itemTitle = args.currentItem.title
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete '$itemTitle'")
        builder.setMessage("Are you sure you want to delete '$itemTitle'?")
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            tasksViewModel.deleteItem(args.currentItem)
            onOperationSuccess("Successfully deleted '$itemTitle'")
        }
        builder.setNegativeButton("No") { _, _ ->
        }
        builder.create().show()
    }

    private fun attemptUpdateItemInDb() {
        val title = binding.titleET.text.toString().trim()
        val priority = binding.spinner.selectedItemPosition
        val time = binding.timeTV.text.toString().trim()
        val date = binding.dateTV.text.toString().trim()
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

        // Date and Time
        val c: Calendar = Calendar.getInstance(Locale.ENGLISH)
        c.set(globalYear, globalMonth, globalDay, globalHour, globalMinute, 0)
        if (c.after(Calendar.getInstance())) {
            setAlarm(c)
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.alarm_set_successfully),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            cancel = true
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.invalid_date),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (time.isBlank() || date.isBlank()) {
            cancel = true
            Toast.makeText(
                requireContext(),
                getString(R.string.date_time_error),
                Toast.LENGTH_SHORT
            ).show()
        }
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
            updateTaskInDb(title, description, time, date, priority)
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
            date = event,
            time = reminder,
            isAlarmChecked = binding.alarmSwitch.isChecked,
            isDone = false
        )
        tasksViewModel.updateData(task)
        onOperationSuccess(getString(R.string.success_update))
    }

    private fun onOperationSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        hideVirtualKeyboard(requireActivity())
        findNavController().navigate(R.id.action_updateTaskFragment_to_tasksListFragment)
    }

    private fun setAlarm(c: Calendar) {
        if (binding.alarmSwitch.isChecked) {
            sharedViewModel.startAlarm(c, requireContext().applicationContext)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.reemhazzaa.daftar.tasks.fragments

import android.app.Application
import android.graphics.Paint
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.reemhazzaa.daftar.R
import com.reemhazzaa.daftar.tasks.data.models.Priority
import com.reemhazzaa.daftar.tasks.data.models.Task
import com.reemhazzaa.daftar.tasks.data.viewModel.TaskViewModel
import com.reemhazzaa.daftar.tasks.fragments.tasksList.TasksListFragmentDirections
import com.reemhazzaa.daftar.tasks.parsePriorityToInt

/**
 * This class contains all custom BindingAdapters for Tasks.
 */
class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("android:navigateToAddFragment")
        fun navigateToAddFragment(bt: Button, navigate: Boolean) {
            bt.setOnClickListener {
                if (navigate) {
                    bt.findNavController()
                        .navigate(R.id.action_tasksListFragment_to_addTaskFragment)
                }
            }
        }

        /**
         * This is a function to observe if the database is empty and
         * show/hide views accordingly.
         */
        @JvmStatic
        @BindingAdapter("android:emptyDatabase")
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
            }
        }

        @JvmStatic
        @BindingAdapter("android:parsePriorityToSpinnerSelection")
        fun parsePriorityToSpinnerSelection(view: Spinner, priority: Priority) {
            when (priority) {
                Priority.HIGH -> view.setSelection(0)
                Priority.MEDIUM -> view.setSelection(1)
                Priority.LOW -> view.setSelection(2)
            }
        }

        @JvmStatic
        @BindingAdapter("android:parseItemBackgroundBasedOnPriority")
        fun parseItemBackgroundBasedOnPriority(view: ConstraintLayout, priority: Priority) {
            when (priority) {
                Priority.HIGH -> view.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.red_light
                    )
                )
                Priority.MEDIUM -> view.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.yellow_light
                    )
                )
                Priority.LOW -> view.setBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.green_light
                    )
                )
            }
        }

        @JvmStatic
        @BindingAdapter("android:parseIndicatorBackgroundBasedOnPriority")
        fun parseIndicatorBackgroundBasedOnPriority(view: CardView, priority: Priority) {
            when (priority) {
                Priority.HIGH -> view.setCardBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.red_dark
                    )
                )
                Priority.MEDIUM -> view.setCardBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.yellow_dark
                    )
                )
                Priority.LOW -> view.setCardBackgroundColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.green_dark
                    )
                )
            }
        }

        @JvmStatic
        @BindingAdapter("android:sendDataToUpdateFragment")
        fun sendDataToUpdateFragment(view: ConstraintLayout, task: Task) {
            view.setOnClickListener {
                val action = TasksListFragmentDirections.actionTasksListFragmentToUpdateTaskFragment(task)
                view.findNavController().navigate(action)
            }
        }

        @JvmStatic
        @BindingAdapter("android:updateTaskIsDone")
        fun updateTaskIsDone(view: CompoundButton, task: Task) {
            view.setOnCheckedChangeListener { buttonView, isChecked ->
                val taskViewModel = TaskViewModel(view.context.applicationContext as Application)
                val updateTask = Task(
                    task.id,
                    task.title,
                    task.priority,
                    task.description,
                    task.time,
                    task.date,
                    task.isAlarmChecked,
                    isChecked
                )
                taskViewModel.updateData(
                    updateTask
                )
            }
        }

        @JvmStatic
        @BindingAdapter("android:strikeTextView")
        fun strikeTextView(view: TextView, drawLine: Boolean) {
            if (drawLine) {
                view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                view.paintFlags = view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }
}
package com.reemhazzaa.daftar.tasks.fragments

import android.view.View
import android.widget.Button
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.reemhazzaa.daftar.R
import com.reemhazzaa.daftar.tasks.data.models.Priority
import com.reemhazzaa.daftar.tasks.data.models.Task

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

    }
}
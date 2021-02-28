package com.reemhazzaa.daftar.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.reemhazzaa.daftar.R

class TasksListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tasks_list, container, false)

        view.findViewById<Button>(R.id.addTaskButton).setOnClickListener {
            findNavController().navigate(R.id.action_tasksListFragment_to_addTaskFragment)
        }

        return view
    }

}
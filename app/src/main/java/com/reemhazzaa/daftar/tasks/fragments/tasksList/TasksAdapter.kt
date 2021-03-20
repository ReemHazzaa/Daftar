package com.reemhazzaa.daftar.tasks.fragments.tasksList

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.reemhazzaa.daftar.R
import com.reemhazzaa.daftar.databinding.ItemTaskBinding
import com.reemhazzaa.daftar.tasks.data.models.Task
import com.reemhazzaa.daftar.tasks.parseIndicatorBackgroundBasedOnPriority
import com.reemhazzaa.daftar.tasks.parseItemBackgroundBasedOnPriority


class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private var list = emptyList<Task>()

    class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.task = task
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): TaskViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTaskBinding.inflate(layoutInflater, parent, false)
                return TaskViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    override fun getItemCount(): Int = list.size

    fun setList(filteredList: List<Task>) {
        list = filteredList
        this.notifyDataSetChanged()
    }

    fun getList(): List<Task> = list

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = list[position]
        holder.bind(currentItem)
    }
}

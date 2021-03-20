package com.reemhazzaa.daftar.tasks.fragments.tasksList.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.reemhazzaa.daftar.databinding.ItemTaskBinding
import com.reemhazzaa.daftar.tasks.data.models.Task


class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private var list = emptyList<Task>()

    class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.task = task
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): TaskViewHolder {
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
        val tasksDiffUtil = TasksDiffUtil(list, filteredList)
        val diffUtilResult = DiffUtil.calculateDiff(tasksDiffUtil)
        list = filteredList
        diffUtilResult.dispatchUpdatesTo(this)
    }

    fun getList(): List<Task> = list

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = list[position]
        holder.bind(currentItem)
    }
}

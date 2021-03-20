package com.reemhazzaa.daftar.tasks.fragments.tasksList.adapters

import androidx.recyclerview.widget.DiffUtil
import com.reemhazzaa.daftar.tasks.data.models.Task

class TasksDiffUtil(
    private val oldList: List<Task>,
    private val newList: List<Task>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return  newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].title == newList[newItemPosition].title
                && oldList[oldItemPosition].description == newList[newItemPosition].description
                && oldList[oldItemPosition].date == newList[newItemPosition].date
                && oldList[oldItemPosition].time == newList[newItemPosition].time
                && oldList[oldItemPosition].priority == newList[newItemPosition].priority
                && oldList[oldItemPosition].isAlarmChecked == newList[newItemPosition].isAlarmChecked
                && oldList[oldItemPosition].isDone == newList[newItemPosition].isDone
    }
}
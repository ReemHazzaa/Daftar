package com.epicsystems.crm.Contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.reemhazzaa.daftar.R
import com.reemhazzaa.daftar.data.tasks.models.Priority
import com.reemhazzaa.daftar.data.tasks.models.Task
import com.reemhazzaa.daftar.tasks.TasksListFragmentDirections
import com.reemhazzaa.daftar.tasks.parseIndicatorBackgroundBasedOnPriority
import com.reemhazzaa.daftar.tasks.parseItemBackgroundBasedOnPriority


class TasksAdapter() : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private lateinit var currentViewHolder: TaskViewHolder
    private var list = listOf<Task>()

    class TaskViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val ctx = itemView.context

        var lightColorCV: CardView = itemView.findViewById(R.id.lightColorCV)
        var lightColorCV2: ConstraintLayout = itemView.findViewById(R.id.lightColorCV2)
        var titleTV: TextView = itemView.findViewById(R.id.titleTV)
        var descTV: TextView = itemView.findViewById(R.id.descTV)
        var darkColorCV: CardView = itemView.findViewById(R.id.darkColorCV)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        currentViewHolder = TaskViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false)
        )

        return currentViewHolder
    }

    override fun getItemCount(): Int = list.size

    fun setList(filteredList: List<Task>) {
        list = filteredList
        this.notifyDataSetChanged()
    }

    fun getList(): List<Task> = list

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val ctx = holder.titleTV.context
        holder.titleTV.text = list[position].title
        holder.descTV.text = list[position].description

        holder.itemView.setOnClickListener {
            val action = TasksListFragmentDirections.actionTasksListFragmentToUpdateTaskFragment(list[position])
            holder.itemView.findNavController().navigate(action)
        }

//        holder.lightColorCV.setCardBackgroundColor(
//            parseItemBackgroundBasedOnPriority(
//                ctx,
//                list[position].priority
//            )
//        )
        holder.lightColorCV2.setBackgroundColor(
            parseItemBackgroundBasedOnPriority(
                ctx,
                list[position].priority
            )
        )
        holder.darkColorCV.setCardBackgroundColor(
            parseIndicatorBackgroundBasedOnPriority(
                ctx,
                list[position].priority
            )
        )

    }
}

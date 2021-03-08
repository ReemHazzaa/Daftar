package com.reemhazzaa.daftar.tasks.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.reemhazzaa.daftar.tasks.data.TasksDatabase
import com.reemhazzaa.daftar.tasks.data.models.Task
import com.reemhazzaa.daftar.tasks.data.repository.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val toDoDao = TasksDatabase.getDatabase(
        application
    ).tasksDao()
    private val repository: TasksRepository = TasksRepository(toDoDao)

    val getAllData: LiveData<List<Task>> = repository.getAllData
    val sortByHighPriority: LiveData<List<Task>> = repository.sortByHighPriority
    val sortByLowPriority: LiveData<List<Task>> = repository.sortByLowPriority

    fun insertData(toDoData: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteItem(toDoData: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(toDoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Task>>{
        return repository.searchDatabase(searchQuery)
    }
}
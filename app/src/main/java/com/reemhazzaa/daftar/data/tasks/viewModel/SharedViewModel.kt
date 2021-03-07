package com.reemhazzaa.daftar.data.tasks.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.reemhazzaa.daftar.data.tasks.models.Task

class SharedViewModel (application: Application): AndroidViewModel(application) {
    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfDatabaseEmpty(toDoData: List<Task>){
        emptyDatabase.value = toDoData.isEmpty()
    }
}
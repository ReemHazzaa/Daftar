package com.reemhazzaa.daftar.tasks.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.reemhazzaa.daftar.tasks.data.models.Task

@Dao
interface TasksDao {

    @Query("SELECT * FROM tasks_table ORDER BY id ASC")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(toDoData: Task)

    @Delete
    suspend fun deleteItemTask(toDoData: Task)

    @Query("DELETE FROM tasks_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM tasks_table WHERE title LIKE :searchQuery")
    fun searchTasksDatabase(searchQuery: String): LiveData<List<Task>>

    @Query("SELECT * FROM tasks_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(): LiveData<List<Task>>
}
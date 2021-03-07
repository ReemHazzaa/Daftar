package com.reemhazzaa.daftar.data.tasks.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tasks_table")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var priority: Priority,
    var description: String,
    var alarm: String,
    var event: String
): Parcelable

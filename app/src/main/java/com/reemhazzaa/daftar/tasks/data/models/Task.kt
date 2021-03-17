package com.reemhazzaa.daftar.tasks.data.models

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
    var time: String,
    var date: String,
    var isAlarmChecked: Boolean,
    var isDone: Boolean
): Parcelable

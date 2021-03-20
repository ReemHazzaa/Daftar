package com.reemhazzaa.daftar.tasks

import android.content.Context
import androidx.core.content.ContextCompat
import com.reemhazzaa.daftar.R
import com.reemhazzaa.daftar.tasks.data.models.Priority

fun parsePriorityIntToString(priority: Int): Priority {
    return when (priority) {
        0 -> Priority.HIGH
        1 -> Priority.MEDIUM
        2 -> Priority.LOW
        else -> Priority.LOW
    }
}

fun parsePriorityToInt(priority: Priority): Int {
    return when(priority) {
        Priority.HIGH -> 0
        Priority.MEDIUM -> 1
        Priority.LOW -> 2
    }
}

fun parseItemBackgroundBasedOnPriority(ctx: Context, priority: Priority): Int {
    return when (priority) {
        Priority.HIGH -> ContextCompat.getColor(ctx, R.color.red_light)
        Priority.MEDIUM -> ContextCompat.getColor(ctx, R.color.yellow_light)
        Priority.LOW -> ContextCompat.getColor(ctx, R.color.green_light)
    }
}

fun parseIndicatorBackgroundBasedOnPriority(ctx: Context, priority: Priority): Int {
    return when (priority) {
        Priority.HIGH -> ContextCompat.getColor(ctx, R.color.red_dark)
        Priority.MEDIUM -> ContextCompat.getColor(ctx, R.color.yellow_dark)
        Priority.LOW -> ContextCompat.getColor(ctx, R.color.green_dark)
    }
}


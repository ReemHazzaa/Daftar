package com.reemhazzaa.daftar.data.tasks

import androidx.room.TypeConverter
import com.reemhazzaa.daftar.data.tasks.models.Priority

class Converter {
    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }
}
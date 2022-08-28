package com.sm.todo.data

import androidx.room.TypeConverter
import com.sm.todo.data.models.Priority

class Converter {

    @TypeConverter
    fun priorityToString(priority: Priority) : String{
        return priority.name
    }

    @TypeConverter
    fun stringToPriority(priority: String) : Priority {
        return Priority.valueOf(priority)
    }

}
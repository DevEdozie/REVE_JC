package com.edozie.reve_jc.local.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.edozie.reve_jc.local.model.TaskPriority
import com.edozie.reve_jc.local.model.TaskStatus
import java.time.LocalDate
import java.time.LocalTime

class Converters {
    @TypeConverter
    fun fromPriority(value: TaskPriority): String = value.name

    @TypeConverter
    fun toPriority(value: String): TaskPriority = TaskPriority.valueOf(value)

    @TypeConverter
    fun fromStatus(value: TaskStatus): String = value.name

    @TypeConverter
    fun toStatus(value: String): TaskStatus = TaskStatus.valueOf(value)

    @TypeConverter
    fun fromLocalDate(value: LocalDate): String = value.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun fromLocalTime(value: LocalTime): String = value.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalTime(value: String): LocalTime = LocalTime.parse(value)
}

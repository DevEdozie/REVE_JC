package com.edozie.reve_jc.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.edozie.reve_jc.local.dao.TaskDao
import com.edozie.reve_jc.local.model.Step
import com.edozie.reve_jc.local.model.Task
import com.edozie.reve_jc.local.util.Converters

@Database(entities = [Task::class, Step::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

package com.edozie.reve_jc.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "steps",
    foreignKeys = [ForeignKey(
        entity = Task::class,
        parentColumns = ["id"],
        childColumns = ["taskId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("taskId")]
)
data class Step(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taskId: Int,
    val description: String,
    val isDone: Boolean = false
)

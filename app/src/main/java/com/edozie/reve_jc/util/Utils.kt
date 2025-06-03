package com.edozie.reve_jc.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.edozie.reve_jc.local.model.Step
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
fun getGreetingMessage(): String {
    val hour = LocalTime.now().hour
    return when (hour) {
        in 0..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }
}

fun calculateProgress(steps: List<Step>): Int {
    if (steps.isEmpty()) return 0
    val doneCount = steps.count { it.isDone }
    return (doneCount * 100) / steps.size
}

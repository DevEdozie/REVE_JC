package com.edozie.reve_jc.util


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.edozie.reve_jc.local.model.Task
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object AlarmUtil {

    @RequiresApi(Build.VERSION_CODES.O)
    fun setAlarmsForTask(context: Context, task: Task) {
        // Alarm for start time
        setAlarm(context, task, isStart = true)
        // Alarm for end time
        setAlarm(context, task, isStart = false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ScheduleExactAlarm")
    fun setAlarm(context: Context, task: Task, isStart: Boolean = true) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val triggerTime = if (isStart) {
            Date.from(task.startDate.atTime(task.startTime).atZone(ZoneId.systemDefault()).toInstant())
        } else {
            Date.from(task.endDate.atTime(task.endTime).atZone(ZoneId.systemDefault()).toInstant())
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("TASK_TITLE", task.title)
            putExtra("TASK_ID", task.id)
            putExtra("message_type", if (isStart) "Start" else "End")
        }

        // Use negative task ID for end alarm to avoid replacing the start alarm
        val requestCode = if (isStart) task.id else -task.id

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime.time,
            pendingIntent
        )
    }

    fun cancelAlarmsForTask(context: Context, taskId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        listOf(taskId, -taskId).forEach { id ->
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }
}




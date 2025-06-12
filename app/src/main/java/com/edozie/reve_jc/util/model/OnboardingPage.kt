package com.edozie.reve_jc.util.model

import androidx.annotation.DrawableRes
import com.edozie.reve_jc.R

data class OnboardingPage(
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String
)

val pages = listOf(
    OnboardingPage(
        R.drawable.task_one_ic, "Stay Organized",
        "Capture all your tasks"
    ),
    OnboardingPage(
        R.drawable.reminder_alert_ic, "Never Miss a Task",
        "Stay on schedule with reminders"
    ),
    OnboardingPage(
        R.drawable.organize_ic, "Sort Your Way",
        "Organize by category"
    ),
)


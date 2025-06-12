package com.edozie.reve_jc.util

import com.edozie.reve_jc.R

sealed class CustomBottomNavBar(
    val route: String,
    val label: String,
    val icon: Int
) {
    object Tasks : CustomBottomNavBar("tasks", "Tasks", R.drawable.tasks_ic)
    object Today : CustomBottomNavBar("today", "Today", R.drawable.today_ic)
    object Profile : CustomBottomNavBar("profile", "Profile", R.drawable.profile_ic)
}
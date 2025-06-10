package com.edozie.reve_jc.util

object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
}

sealed class Screen(val route: String) {
    //    object Home : Screen("home")
    object AddTask : Screen("add_task")
    object TaskDetail : Screen("task_detail/{taskId}") {
        fun createRoute(taskId: Int) = "task_detail/$taskId"
    }

    object Today : Screen("today")
    object EditTask : Screen("edit_task/{taskId}") {
        fun createRoute(taskId: Int) = "edit_task/$taskId"
    }
}

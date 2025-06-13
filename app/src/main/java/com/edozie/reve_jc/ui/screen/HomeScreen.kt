package com.edozie.reve_jc.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.edozie.reve_jc.RequestNotificationPermissionIfNeeded
import com.edozie.reve_jc.ui.widget.CustomBottomNavigationBar
import com.edozie.reve_jc.util.CustomBottomNavBar
import com.edozie.reve_jc.util.NetworkObserver
import com.edozie.reve_jc.util.Routes
import com.edozie.reve_jc.util.Screen
import com.edozie.reve_jc.util.model.pages
import com.edozie.reve_jc.viewmodel.TaskViewModel

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(networkObserver: NetworkObserver) {
    RequestNotificationPermissionIfNeeded()


    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val userCurrentRoute = currentBackStackEntry?.destination?.route


    val screenTitle = when (userCurrentRoute) {
        Screen.AddTask.route -> "Add Task"
        CustomBottomNavBar.Tasks.route -> "Tasks"
        Screen.TaskDetail.route -> "Task Detail"
        Screen.EditTask.route -> "Edit Task"
        else -> ""
    }

    val shouldShowTitle = userCurrentRoute in setOf(
        Screen.AddTask.route,
        CustomBottomNavBar.Tasks.route,
        Screen.TaskDetail.route,
        Screen.EditTask.route,
    )

    val shouldShowBackArrow =
        userCurrentRoute == Screen.AddTask.route || userCurrentRoute == Screen.TaskDetail.route || userCurrentRoute == Screen.EditTask.route

    val showBottomBar = when {
        userCurrentRoute == CustomBottomNavBar.Tasks.route -> true
        userCurrentRoute == CustomBottomNavBar.Today.route -> true
        userCurrentRoute == CustomBottomNavBar.Profile.route -> true
        else -> false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    if (shouldShowBackArrow) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                title = {
                    if (shouldShowTitle) {
                        Text(
                            text = screenTitle,
                            color = Color.Black,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            letterSpacing = 0.15.sp,
                            lineHeight = 28.sp
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                CustomBottomNavigationBar(
                    currentDestination = userCurrentRoute ?: "",
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Avoid multiple copies of the same destination
                            popUpTo(CustomBottomNavBar.Tasks.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier
                .padding(paddingValues)
        ) {
            // Splash screen
            composable(Routes.SPLASH) { SplashScreen(navController) }
            // Auth screens : Start
            composable(Routes.SIGNUP) {
                SignupScreen(
                    navController,
                    networkObserver = networkObserver
                )
            }
            composable(Routes.LOGIN) {
                LoginScreen(
                    navController,
                    networkObserver = networkObserver
                )
            }
            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    navController, pages = pages,
                    onLoginClick = { /* navigate to Login */ },
                    onCreateClick = { /* navigate to SignUp */ },
                    onGoogleClick = { /* launch Google sign-in */ })
            }
            // Auth screens : End
            // Bottom bar screens: Start
            composable(CustomBottomNavBar.Tasks.route) { TasksScreen(navController = navController) }
            composable(CustomBottomNavBar.Today.route) {
                TodayTasksScreen(
                    todayTasks = listOf(),
                    onTaskClick = {}
                )
            }
            composable(CustomBottomNavBar.Profile.route) { ProfileScreen() }
            // Bottom bar screens: End

            // App functionality screens: Start
            composable(Screen.AddTask.route) { AddTaskScreen(navController = navController) }
            composable(
                route = Screen.TaskDetail.route, arguments = listOf(
                    navArgument("taskId") { type = NavType.IntType }
                )) { backStack ->
                val vm: TaskViewModel = hiltViewModel()
                val taskId = backStack.arguments!!.getInt("taskId")
                LaunchedEffect(taskId) {
                    vm.loadTask(taskId)
                    vm.loadSteps(taskId)
                }

                // guard against null
                val task by vm.currentTask.collectAsState()
                if (task != null) {
                    TaskDetailScreen(vm, navController = navController)
                } else {
                    // fallback while loading
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
            composable(
                route = Screen.EditTask.route, arguments = listOf(
                    navArgument("taskId") { type = NavType.IntType }
                )) { backStack ->
                val vm: TaskViewModel = hiltViewModel()
                val taskId = backStack.arguments!!.getInt("taskId")
                LaunchedEffect(taskId) {
                    vm.loadTask(taskId)
                    vm.loadSteps(taskId)
                }

                // guard against null
                val task by vm.currentTask.collectAsState()
                if (task != null) {
                    EditTaskScreen(vm)
                } else {
                    // fallback while loading
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

        }
    }

}
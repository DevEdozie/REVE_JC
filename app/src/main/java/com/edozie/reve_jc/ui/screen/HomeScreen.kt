package com.edozie.reve_jc.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.edozie.reve_jc.RequestNotificationPermissionIfNeeded
import com.edozie.reve_jc.ui.widget.CustomBottomNavigationBar
import com.edozie.reve_jc.util.CustomBottomNavBar
import com.edozie.reve_jc.util.NetworkObserver
import com.edozie.reve_jc.util.Routes
import com.edozie.reve_jc.util.Screen
import com.edozie.reve_jc.util.model.pages

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
        else -> ""
    }

    val shouldShowTitle = userCurrentRoute == Screen.AddTask.route

    val shouldShowBackArrow = userCurrentRoute == Screen.AddTask.route

    val showBottomBar = when {
        userCurrentRoute == CustomBottomNavBar.Tasks.route -> true
        userCurrentRoute == CustomBottomNavBar.Today.route -> true
        userCurrentRoute == CustomBottomNavBar.Profile.route -> true
        else -> false
    }

    Scaffold(
        topBar = {
            TopAppBar(
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
            composable(Routes.SPLASH) { SplashScreen(navController) }
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
            composable(CustomBottomNavBar.Tasks.route) { TasksScreen(navController = navController) }
            composable(CustomBottomNavBar.Today.route) { TodayScreen() }
            composable(CustomBottomNavBar.Profile.route) { ProfileScreen() }
            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    navController, pages = pages,
                    onLoginClick = { /* navigate to Login */ },
                    onCreateClick = { /* navigate to SignUp */ },
                    onGoogleClick = { /* launch Google sign-in */ })
            }
            composable(Screen.AddTask.route) { AddTaskScreen() }
        }
    }

}
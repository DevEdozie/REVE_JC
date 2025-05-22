package com.edozie.reve_jc.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.edozie.reve_jc.ui.widget.CustomBottomNavigationBar
import com.edozie.reve_jc.util.CustomBottomNavBar
import com.edozie.reve_jc.util.NetworkObserver
import com.edozie.reve_jc.util.model.pages

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(networkObserver: NetworkObserver) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val userCurrentRoute = currentBackStackEntry?.destination?.route


    val showBottomBar = when {
        userCurrentRoute == CustomBottomNavBar.Assets.route -> true
        userCurrentRoute == CustomBottomNavBar.Earn.route -> true
        userCurrentRoute == CustomBottomNavBar.Updates.route -> true
        userCurrentRoute == CustomBottomNavBar.Profile.route -> true
        else -> false
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                CustomBottomNavigationBar(
                    currentDestination = userCurrentRoute ?: "",
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Avoid multiple copies of the same destination
                            popUpTo(CustomBottomNavBar.Assets.route) { saveState = true }
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
            startDestination = "splash",
            modifier = Modifier
                .padding(paddingValues)
        ) {
            composable("splash") { SplashScreen(navController) }
            composable("signup") { SignupScreen(navController, networkObserver = networkObserver) }
            composable("login") { LoginScreen(navController, networkObserver = networkObserver) }
            composable(CustomBottomNavBar.Assets.route) { AssetsScreen() }
            composable(CustomBottomNavBar.Earn.route) { EarnScreen() }
            composable(CustomBottomNavBar.Updates.route) { UpdatesScreen() }
            composable(CustomBottomNavBar.Profile.route) { ProfileScreen() }
            composable("onboarding") {
                OnboardingScreen(
                    navController, pages = pages,
                    onLoginClick = { /* navigate to Login */ },
                    onCreateClick = { /* navigate to SignUp */ },
                    onGoogleClick = { /* launch Google sign-in */ })
            }
        }
    }

}
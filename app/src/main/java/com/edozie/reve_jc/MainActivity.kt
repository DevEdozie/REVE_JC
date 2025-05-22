package com.edozie.reve_jc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.edozie.reve_jc.ui.screen.HomeScreen
import com.edozie.reve_jc.ui.screen.OnboardingScreen
import com.edozie.reve_jc.ui.theme.REVE_JCTheme
import com.edozie.reve_jc.util.model.pages

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
//            OnboardingScreen(
//                pages = pages,
//                onLoginClick = { /* navigate to Login */ },
//                onCreateClick = { /* navigate to SignUp */ },
//                onGoogleClick = { /* launch Google sign-in */ }
//            )
        }
    }
}


package com.edozie.reve_jc

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import com.edozie.reve_jc.util.NetworkObserver
import com.edozie.reve_jc.util.model.pages
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkObserver: NetworkObserver

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(networkObserver = networkObserver)
        }
    }
}


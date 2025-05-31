package com.edozie.reve_jc.ui.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.edozie.reve_jc.R
import com.edozie.reve_jc.ui.widget.CustomTextField
import com.edozie.reve_jc.util.AuthState
import com.edozie.reve_jc.util.NetworkObserver
import com.edozie.reve_jc.util.Routes
import com.edozie.reve_jc.viewmodel.AuthViewModel

@Composable
fun SignupScreen(
    navController: NavController,
    networkObserver: NetworkObserver,
    vm: AuthViewModel = hiltViewModel(),
) {

    val state by vm.state.collectAsState()
    val email by vm.email.collectAsState()
    val password by vm.password.collectAsState()
    val context = LocalContext.current

    // Local UI state for validation error
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }


    // Navigation side-effect
    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) {
            navController.navigate(Routes.LOGIN) {
                popUpTo(Routes.SIGNUP) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // SECTION 1
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(R.drawable.organize_ic),
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "New Here? Let’s Get Organized",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Text("Already have an account? ")
                Text(
                    text = "Log in",
                    color = Color(0xFF04F6DA),
                    modifier = Modifier.clickable {
                        /* navigate to login */
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.SIGNUP) { inclusive = true }
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            CustomTextField(
                value = email,
                onValueChange = {
                    vm.onEmailChange(it)
                    emailError = null
                },
                placeholder = "Email address",
                isPassword = false,
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (emailError != null) {
                Text(
                    text = emailError ?: "",
                    color = Color.Red,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            CustomTextField(
                value = password,
                onValueChange = {
                    vm.onPasswordChange(it)
                    passwordError = null
                },
                placeholder = "Enter a password",
                isPassword = true,
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (passwordError != null) {
                Text(
                    text = passwordError ?: "",
                    color = Color.Red,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            // Show Firebase error if any
            if (state is AuthState.Error) {
                Text(
                    (state as AuthState.Error).message,
                    color = Color.Red,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            // Continue Button
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    // Empty‑field check (email)
                    if (email.isBlank()) {
                        emailError = "Email cannot be empty"
                        return@Button
                    }
                    // Empty‑field check (password)
                    if (password.isBlank()) {
                        passwordError = "Password cannot be empty"
                        return@Button
                    }

                    // Connectivity check
                    if (!networkObserver.isOnline()) {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // All good
                    vm.signUp(email.trim(), password.trim())
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (state is AuthState.Loading) {
                    CircularProgressIndicator(Modifier.size(24.dp))
                } else {
                    Text(
                        "Continue", color = Color.White, style = TextStyle(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // OR Divider

//            Text(
//                "Or", modifier = Modifier.padding(vertical = 8.dp), style = TextStyle(
//                    fontWeight = FontWeight.Bold
//                )
//            )

            // Google Button
//            OutlinedButton(
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.White
//                ),
//                shape = RoundedCornerShape(8.dp),
//                onClick = {},
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Image(
//                    painter = painterResource(R.drawable.google_ic),
//                    contentDescription = null,
//                    modifier = Modifier.size(20.dp)
//                )
//                Spacer(Modifier.width(8.dp))
//                Text(
//                    "Use Google instead", color = Color.Black, style = TextStyle(
//                        fontWeight = FontWeight.Bold
//                    )
//                )
//            }

//            Spacer(modifier = Modifier.height(16.dp))

        }

        // SECTION 2
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Terms
            Text(
                text = "By creating an account, you accept our ",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "terms and conditions",
                color = Color(0xFF04F6DA),
                style = MaterialTheme.typography.bodySmall.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable { /* open terms */ }
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview() {
//    SignupScreen()
}
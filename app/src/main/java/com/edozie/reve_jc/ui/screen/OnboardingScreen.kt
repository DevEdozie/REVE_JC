package com.edozie.reve_jc.ui.screen

import android.widget.Space
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edozie.reve_jc.R
import com.edozie.reve_jc.ui.widget.PageContent
import com.edozie.reve_jc.util.model.OnboardingPage
import com.edozie.reve_jc.util.model.pages


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    pages: List<OnboardingPage>,
    onLoginClick: () -> Unit,
    onCreateClick: () -> Unit,
    onGoogleClick: () -> Unit
) {

    // Pager state
    val pagerState = rememberPagerState(
        pageCount = {
            pages.size
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            PageContent(pages[page])
        }

        // Dots indicator
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (isSelected) 12.dp else 8.dp)
                        .background(
                            color = if (isSelected) Color.Black else Color.Gray,
                            shape = CircleShape
                        )
                )
            }
        }

        // Bottom buttons
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF04F6DA)

            ),
            shape = RoundedCornerShape(8.dp),
            onClick = {
                navController.navigate("login") {
                    popUpTo("onboarding") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Log in to existing wallet,",
                color = Color.Black,
                style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(Modifier.height(8.dp))
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF04F6DA)
            ),
            shape = RoundedCornerShape(8.dp),
//            onClick = onCreateClick,
            onClick = {
                navController.navigate("signup") {
                    popUpTo("onboarding") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Create new wallet", color = Color.Black, style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            onClick = onGoogleClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.google_ic),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Continue with Google", color = Color.Black, style = TextStyle(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingPagePreview() {
//    OnboardingScreen(
//        pages = pages,
//        onLoginClick = { /* navigate to Login */ },
//        onCreateClick = { /* navigate to SignUp */ },
//        onGoogleClick = { /* launch Google sign-in */ }
//    )
}
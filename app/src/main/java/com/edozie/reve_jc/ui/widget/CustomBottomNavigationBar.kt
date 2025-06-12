package com.edozie.reve_jc.ui.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.edozie.reve_jc.util.CustomBottomNavBar


@Composable
fun CustomBottomNavigationBar(
    currentDestination: String,
    onNavigate: (String) -> Unit
) {

    val bottomNavItems = listOf(
        CustomBottomNavBar.Tasks,
        CustomBottomNavBar.Today,
        CustomBottomNavBar.Profile,
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        containerColor = Color.White.copy(alpha = 0.85f),
        tonalElevation = 8.dp
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentDestination == item.route


            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(text = item.label) },
                selected = selected,
                onClick = { onNavigate(item.route) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color(0xFF6D6D6D),
                    indicatorColor = Color(0xFFF0F0F0)
                )
            )

        }

    }


}
package com.edozie.reve_jc.util

import com.edozie.reve_jc.R

sealed class CustomBottomNavBar(
    val route: String,
    val label: String,
    val icon: Int
) {
    object Assets : CustomBottomNavBar("assets", "Assets", R.drawable.assets_bar_ic)
    object Earn : CustomBottomNavBar("earn", "Earn", R.drawable.earn_bar_ic)
    object Updates : CustomBottomNavBar("updates", "Updates", R.drawable.updates_bar_ic)
    object Profile : CustomBottomNavBar("profile", "Profile", R.drawable.profile_bar_ic)
}
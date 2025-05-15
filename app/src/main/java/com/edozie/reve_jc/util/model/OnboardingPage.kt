package com.edozie.reve_jc.util.model

import androidx.annotation.DrawableRes
import com.edozie.reve_jc.R

data class OnboardingPage(
    @DrawableRes val imageRes: Int,
    val title: String,
    val description: String
)

val pages = listOf(
    OnboardingPage(
        R.drawable.wallet_ob_ic, "Your Wallet",
        "A better way of engaging with apps and services on the decentralized web"
    ),
    OnboardingPage(
        R.drawable.kite_ob_ic, "Nothing New To Learn",
        "Use your crypto the same way you would normally use fiat"
    ),
    OnboardingPage(
        R.drawable.word_ob_ic, "Send, Receive, And Convert Crypto",
        "Send and receive crypto. Convert to fiat and vice verse easily and quickly"
    ),
    OnboardingPage(
        R.drawable.rocket_ob_ic, "Put Your Assets To Work",
        "Invest your asset into our pools and earn juicy yields"
    ),
    OnboardingPage(
        R.drawable.lock_ob_ic, "As Secure As Possible",
        "You data is safe with us. We regularly conduct security audits on our app "
    )
)


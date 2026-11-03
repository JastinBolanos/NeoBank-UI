package com.neobank.app.core.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun KmpBackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    BackHandler(enabled = enabled, onBack = onBack)
}
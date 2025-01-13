package ru.paylab.core.designsystem.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RssLoadingScreen(modifier: Modifier) {
    Box(modifier = Modifier
        .fillMaxSize()
        .clickable { }
        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
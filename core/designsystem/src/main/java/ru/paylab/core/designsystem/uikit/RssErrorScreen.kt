package ru.paylab.core.designsystem.uikit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.paylab.core.designsystem.R


@Composable
fun RssErrorScreen() {
    println("ErrorScreen")
    Text(
        text = stringResource(R.string.error),
        modifier = Modifier.fillMaxSize()
    )
}
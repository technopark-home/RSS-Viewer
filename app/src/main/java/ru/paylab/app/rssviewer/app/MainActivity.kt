package ru.paylab.app.rssviewer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.paylab.core.datastore.ColorMode
import ru.paylab.core.designsystem.theme.RssViewerTheme

private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        var uiState: MainUiState by mutableStateOf(MainUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.onEach { uiState = it }.collect { uiState = it }
            }
        }
        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                MainUiState.Loading -> true
                is MainUiState.Success -> false
            }
        }

        enableEdgeToEdge()
        setContent {
            RssViewerTheme(
                darkTheme = isUseDarkTheme(uiState),
                dynamicColor = useDynamicTheme(uiState),
            ) {
                RichSiteSummaryViewScreen()
            }
        }
    }
}

@Composable
private fun isUseDarkTheme(state: MainUiState): Boolean = when (state) {
    MainUiState.Loading -> isSystemInDarkTheme()
    is MainUiState.Success -> when (state.colorMode) {
        ColorMode.SYSTEM -> isSystemInDarkTheme()
        ColorMode.DARK -> true
        ColorMode.LIGHT -> false
    }
}


@Composable
private fun useDynamicTheme(uiState: MainUiState): Boolean = when (uiState) {
    MainUiState.Loading -> false
    is MainUiState.Success -> uiState.useDynamicColor
}

///// KIT
// Function to generate a Toast

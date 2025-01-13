package ru.paylab.feature.downloadedarticle

import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView


@Composable
internal fun SavedViewScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    clearFiles: () -> Unit,
    fileName: String,
) {
    val context = LocalContext.current

    val webView = remember {
        WebView(context).apply {
            webViewClient = object: WebViewClient(){}
            webChromeClient = object: WebChromeClient(){}
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.allowContentAccess = true
            settings.allowFileAccess = true
            settings.loadsImagesAutomatically = true
            settings.loadWithOverviewMode = false
            settings.useWideViewPort = false
        }
    }
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Button(
                    onClick = {
                        onNavigateUp()
                    }, modifier = Modifier
                        .wrapContentHeight()
                ) { Text(stringResource(R.string.exit)) }
                Button(
                    onClick = {
                        clearFiles()
                        onNavigateUp()
                    }, modifier = Modifier
                        .wrapContentHeight()
                ) { Text(stringResource(R.string.delete)) }
            }
            Box(
                modifier = Modifier
                    .weight(2F)
            ) {
                AndroidView(factory = { webView }, update = {
                    it.loadUrl("file:///$fileName")
                })
            }
        }
    }
}
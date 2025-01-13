package ru.paylab.feature.downloaded

import android.graphics.Bitmap
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.paylab.core.designsystem.uikit.RssErrorScreen
import ru.paylab.core.designsystem.uikit.RssLoadingScreen
import java.io.File
import kotlin.math.roundToInt

@Composable
internal fun WebViewSave(
    modifier: Modifier,
    onNavigateUp: () -> Unit,
    viewModel: LocalCacheViewModel = hiltViewModel<LocalCacheViewModel>(),
    onSaveImg: (Int,String) -> Unit,
) {
    println("ArticleId: ${viewModel.selectedArticleId} ")

    val articleStateUI: ArticlesUiState by viewModel.selectedArticle.collectAsStateWithLifecycle()
    when (val article = articleStateUI) {
        is ArticlesUiState.Error -> RssErrorScreen()
        is ArticlesUiState.Loading -> RssLoadingScreen(modifier)
        is ArticlesUiState.Success -> {
            WebViewSavable(
                modifier = modifier,
                onNavigateUp = onNavigateUp,
                articleDocFilename = viewModel.getDocFileName(viewModel.selectedArticleId),
                onSaveImg = { onSaveImg( article.articleId, article.imageUrl ) },
                link = article.link,
            )
        }
    }
}

@Composable
internal fun WebViewSavable(
    modifier: Modifier,
    onNavigateUp: () -> Unit,
    onSaveImg: () -> Unit,
    articleDocFilename: String,
    link: String,
) {
    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(false) }

    val progress = remember { mutableFloatStateOf(0.0F) }
    val webView = remember {
        WebView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            //settings.javaScriptEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.allowContentAccess = true
            settings.allowFileAccess = true
            settings.loadsImagesAutomatically = true

            settings.loadWithOverviewMode = false
            settings.useWideViewPort = false

            webViewClient = object: WebViewClient(){
                override fun onPageStarted(
                    view: WebView, url: String,
                    favicon: Bitmap?) {
                    isLoading.value = true
                }

                override fun onPageFinished(
                    view: WebView, url: String) {
                    progress.floatValue = view.progress.toFloat()
                    if( view.progress == 100 )
                        isLoading.value = false
                }
            }
            /*webChromeClient = object: WebChromeClient(){
                override fun onProgressChanged( view: WebView, newProgress: Int) {
                }
            }*/
            loadUrl(link)
        }
    }
    Box(
        modifier = modifier.fillMaxSize(),
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                //verticalAlignment = Alignment.

            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .wrapContentHeight(Alignment.CenterVertically),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    //verticalAlignment = Alignment.Start,
                ) {
                    Button(
                        onClick = { onNavigateUp()}) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Button(
                        enabled = !isLoading.value,
                        onClick = {
                            webView.saveWebArchive(articleDocFilename, false) { _ ->
                                onSaveImg()
                                onNavigateUp()
                            }
                        }) {
                        Text(text = stringResource(R.string.save))
                    }
                }

                if (isLoading.value){
                    CircularProgressIndicator()
                    Text(
                        text = "${progress.floatValue.roundToInt()}%",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(2F)
            ) {
                AndroidView(factory = { webView },
                    update = {})
            }
        }
    }
}
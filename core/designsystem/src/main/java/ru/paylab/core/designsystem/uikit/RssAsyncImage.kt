package ru.paylab.core.designsystem.uikit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ru.paylab.core.designsystem.R

@Composable
fun RssAsyncImage(
    imageUrl: String,
    modifier: Modifier,
) {
    val request: Any =
        if (imageUrl.isNotEmpty()) ImageRequest.Builder(LocalContext.current).data(imageUrl)
            .crossfade(true).build()
        else R.drawable.no_image

    AsyncImage(
        model = request,
        modifier = modifier,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        //contentScale = ContentScale.Crop,
        onLoading = {
            //println("onLoading $imageUrl")
            //CircularProgressIndicator(modifier = Modifier.requiredSize(40.dp))
        },
        error = painterResource(R.drawable.baseline_broken_image_24),
        placeholder = painterResource(R.drawable.baseline_downloading_24),

        onSuccess = {/*println("onSuccess $imageUrl")*/ },
        onError = { /*println("onError $imageUrl")*/ },
    )
}
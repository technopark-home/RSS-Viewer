package ru.paylab.feature.articledescription

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.paylab.core.designsystem.utils.RssViewerIcons
import ru.paylab.core.designsystem.uikit.RssAsyncImage
import ru.paylab.core.designsystem.utils.rssConvertDate
import ru.paylab.core.model.data.ArticleCategories

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ArticleDescriptionCardScreen(
    modifier: Modifier = Modifier,
    onSave: (Int, Boolean) -> Unit,
    onSavedView: (Int) -> Unit,
    onBookMarkToggle: (Int, Boolean) -> Unit,
    onFavoriteToggle: (Int, Boolean) -> Unit,
    articleCategories: ArticleCategories,
) {
    println("ArticleDescriptionCardScreen")
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            val htmlBody = articleCategories.description.replace("<img.+/(img)*>".toRegex(), "")
            val res = articleCategories.imageUrl
            Row(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.weight(0.9f),
                    text = articleCategories.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (articleCategories.isRead) {
                        FontWeight.Normal
                    } else {
                        FontWeight.Bold
                    },
                    textAlign = TextAlign.Justify
                )
                FilledIconToggleButton(checked = articleCategories.bookmark, onCheckedChange = { _ ->
                    onBookMarkToggle(articleCategories.id, !articleCategories.bookmark)
                }) {
                    Icon(imageVector = RssViewerIcons.Bookmark, contentDescription = "BookMark")
                }
                FilledIconToggleButton(
                    modifier = Modifier.weight(0.1f),
                    checked = articleCategories.isSaved,
                    onCheckedChange = {
                        onSave(articleCategories.id, !articleCategories.isSaved)
                    },
                ) {
                    Icon( imageVector = RssViewerIcons.Saved, contentDescription = "Download" )
                }
            }
            RssAsyncImage(
                imageUrl = res,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp)),
            )
            /*val request: Any = if( res.isNotEmpty() )
                ImageRequest.Builder(LocalContext.current).data(res).crossfade(true)
                    .build()
            else
                R.drawable.no_image
            AsyncImage(
                model = request,

                contentDescription = null,
                contentScale = ContentScale.Fit,
                //contentScale = ContentScale.Crop,
                onLoading = {
                    println("Loading $res")
                    //CircularProgressIndicator(modifier = Modifier.requiredSize(40.dp))
                },
                error = painterResource(R.drawable.baseline_broken_image_24),
                placeholder = painterResource(R.drawable.baseline_downloading_24),

                onSuccess = { println("onSuccess $res") },
                onError = { println("onError $res") },
            )*/
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(6.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = articleCategories.creator,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = rssConvertDate(articleCategories.pubDate),
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            val styledAnnotatedString = AnnotatedString.fromHtml(htmlString = htmlBody)
            val basicTextStyle =
                LocalTextStyle.current.merge(TextStyle(color = LocalContentColor.current))
            BasicText(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onSavedView(articleCategories.id) },
                text = styledAnnotatedString,
                style = basicTextStyle,
            )
            println("Card ${articleCategories.categories.size}")
            FlowRow(
                Modifier.fillMaxWidth().padding(2.dp).wrapContentHeight(align = Alignment.Top),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                overflow = FlowRowOverflow.Visible,
                maxItemsInEachRow = 60,
            ) {
                articleCategories.categories.forEach { category ->
                    println("category ${category.id}")
                    val containerColor = if (category.categoryFavorite) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                    OutlinedButton(
                        onClick = {onFavoriteToggle(category.id, !category.categoryFavorite)},
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = containerColor,
                            contentColor = contentColorFor(backgroundColor = containerColor),
                            disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                        ),
                    ) {
                        println("category ${category.name}")
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.labelSmall,
                            softWrap = false,
                            maxLines = 1,
                            modifier = Modifier.wrapContentSize().padding(3.dp)
                        )
                    }
                }
            }
            println("Card Fin 0")
        }
    }
    println("Card Fin")
}
package ru.paylab.feature.articlesviewer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.paylab.core.designsystem.uikit.RssAsyncImage
import ru.paylab.core.designsystem.utils.RssViewerIcons
import ru.paylab.core.designsystem.utils.rssConvertDate
import ru.paylab.core.model.data.Article

@Composable
internal fun ArticleCard(
    article: Article,
    onItemClick: (Int) -> Unit,
    expandedItemId: Int,
    onSave: (Int, Boolean) -> Unit,
    onBodyClick: (Int) -> Unit,
    onBookMark: (Int, Boolean) -> Unit,
) {
    val sizeImg = animateFloatAsState(
        targetValue = if (article.id == expandedItemId) 1f else 0.3f, label = ""
    )
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .testTag("ArticleCard"),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        val htmlBody = article.description.replace("<img.+/(img)*>".toRegex(), "")
        val res = article.imageUrl
        Row(modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()) {
            Text(
                modifier = Modifier.weight(0.9f),
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (article.isRead) {
                    FontWeight.Normal
                } else {
                    FontWeight.Bold
                },
                textAlign = TextAlign.Justify
            )
            FilledIconToggleButton(
                modifier = Modifier.testTag("Bookmark"),
                checked = article.bookmark,
                onCheckedChange = {
                    onBookMark(article.id, !article.bookmark)
                }
            ) {
                Icon(imageVector = RssViewerIcons.Bookmark, contentDescription = "Bookmark")
            }
            FilledIconToggleButton(
                modifier = Modifier.weight(0.1f),
                checked = article.isSaved,
                onCheckedChange = {
                    onSave(article.id, !article.isSaved)
                },
            ) {
                Icon(imageVector = RssViewerIcons.Saved, contentDescription = "Download")
            }
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { onItemClick(article.id) }
                .testTag("article:${article.id}"),
        ) {
            RssAsyncImage(
                imageUrl = res,
                modifier = Modifier
                    .fillMaxWidth(fraction = sizeImg.value)
                    .height((250 * sizeImg.value).dp)
                    .clip(RoundedCornerShape(6.dp)),
            )
            if (article.id != expandedItemId) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(6.dp),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = article.creator,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )
                    Text(
                        text = rssConvertDate(article.pubDate),
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = article.id == expandedItemId,
        ) {
            val styledAnnotatedString = AnnotatedString.fromHtml(htmlString = htmlBody)
            val basicTextStyle =
                LocalTextStyle.current.merge(TextStyle(color = LocalContentColor.current))
            BasicText(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onBodyClick(article.id) },
                text = styledAnnotatedString,
                style = basicTextStyle,
            )
        }
    }
}
package ru.paylab.feature.articlesviewer

/*
@Composable
internal fun ArticleViewExpanded(
    article: Article,
    onItemClick: (Int) -> Unit,
    expandedItemId: Int,
    onSave: (Int, Boolean) -> Unit,
    onSavedView: (Int) -> Unit,
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
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        val htmlBody = article.description.replace("<img.+/(img)*>".toRegex(), "")
        /*val htmlBody = article.description.replace("<img.+/(img)*>".toRegex(), "")
        val urlString = "https?://[^\"]*?\\.(png|jpe?g|img)".toRegex()
        val imgUrl = "<img.+/(img)*>".toRegex().find(article.descriptionHtml)
        val res = imgUrl?.let { urlString.find(it.value)?.value } ?: ""*/
        println("Card image: ${article.imageUrl}")
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
            //Spacer(Modifier. weight(1f))
            //var checked by remember { mutableStateOf(false) }
            FilledIconToggleButton(checked = article.bookMark, onCheckedChange = {
                onBookMark(article.id, !article.bookMark)
            }) {
                Icon(imageVector = RssViewerIcons.Bookmark, contentDescription = "BookMark")
            }
            FilledIconToggleButton(
                modifier = Modifier.weight(0.1f),
                checked = article.isSaved,
                onCheckedChange = {
                    onSave(article.id, !article.isSaved)
                },
            ) {
                Icon(imageVector = RssViewerIcons.Saved, contentDescription = "Download")
                /*if (article.isSaved) {
                    Icon(
                        painter = painterResource(R.drawable.download),
                        contentDescription = "Download"
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.download),
                        contentDescription = "Clear form cache"
                    )
                }*/
            }
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { onItemClick(article.id) },
        ) {
            println("Load Img: $res")
            val request: Any =
                if (res.isNotEmpty()) ImageRequest.Builder(LocalContext.current).data(res)
                    .crossfade(true).build()
                else R.drawable.no_image
            AsyncImage(
                model = request,
                modifier = Modifier
                    //.fillMaxWidth((.35f))
                    .fillMaxWidth(fraction = sizeImg.value)
                    .height((250 * sizeImg.value).dp)
                    .clip(RoundedCornerShape(6.dp)),
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
                    val convertDate = @Composable
                    fun(time: Long): String {
                        val instant: Instant = Instant.fromEpochMilliseconds(time * 1000)/*@Composable
                        fun dateFormatted(instant: Instant): String = DateTimeFormatter*/


                        // Convert Instant to LocalDateTime
                        /*val formatter: String? = DateTimeFormatter
                            .ofLocalizedDate(FormatStyle.MEDIUM)
                            .withLocale(Locale.getDefault())
                            .withZone(ZoneId.systemDefault())
                            .format(instant.toJavaInstant())
                        return formatter?:""*/
                        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            .withZone(ZoneId.systemDefault()).format(instant.toJavaInstant())
                    }
                    Text(
                        text = convertDate(article.pubDate),
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
                    .clickable { onSavedView(article.id) },
                text = styledAnnotatedString,
                style = basicTextStyle,
            )
        }
    }
}*/
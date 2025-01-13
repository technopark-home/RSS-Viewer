package ru.paylab.core.network

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory


class FeedParser {
    private val pullParserFactory = XmlPullParserFactory.newInstance()
    private val parser = pullParserFactory.newPullParser()

    fun parse(xml: String): ArticleFeed {
        parser.setInput(xml.byteInputStream(), null)

        val articlesFeed = mutableListOf<FeedItem>()

        var feedTitle = ""

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "title") {
                feedTitle = readText(parser)
            } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "item") {
                articlesFeed.add(readFeedItem(parser))
            }
            parser.next()
        }

        return ArticleFeed(
            feedItem = articlesFeed,
            feedTitle = feedTitle,
        )
    }

    private fun readFeedItem(parser: XmlPullParser): FeedItem {
        var title = ""
        var link = ""
        var author = ""
        var pubDate = ""
        var image = ""
        var description = ""
        val categories: MutableList<String> = mutableListOf()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "title") {
                title = readText(parser)
            } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "link") {
                link = readText(parser)
            } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "dc:creator") {
                author = readText(parser)
            } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "pubDate") {
                pubDate = readText(parser)
            } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "description") {
                description = readText(parser)
                val urlString = "https?://[^\"]*?\\.(png|jpe?g|img)".toRegex()
                val imgUrl = "<img.+/(img)*>".toRegex()
                    .find(description)
                image = imgUrl?.let { urlString.find(it.value)?.value } ?: ""
            } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "category") {
                categories.add(readText(parser))
            } else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "enclosure") {
                if(parser
                    .getAttributeValue( null,"type")
                    .trim()
                    .startsWith("image"))
                    image = parser.getAttributeValue( null,"url").trim()
                skipTag(parser)
            }  else if (parser.eventType == XmlPullParser.START_TAG && parser.name == "cover_image") {
                image = readText(parser)
            } else if (parser.eventType == XmlPullParser.START_TAG) {
                skipTag(parser)
            }
        }

        return FeedItem(
            title = title,
            link = link,
            creator = author,
            pubDate = pubDate,
            description = description,
            categories = categories,
            image = image
        )
    }

    private fun readText(parser: XmlPullParser): String {
        var text = ""
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType == XmlPullParser.TEXT) {
                text = parser.text
            }
        }
        return text
    }

    private fun skipTag(parser: XmlPullParser) {
        while (parser.next() != XmlPullParser.END_TAG) {
            // do nothing
        }
    }
}
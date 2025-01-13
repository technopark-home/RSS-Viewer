package ru.paylab.feature.infonavbar

import ru.paylab.core.designsystem.utils.RouteNavigation
import ru.paylab.core.designsystem.utils.RssViewerIcons

internal object BottomNavigationItems {
    val RssBottomNavigationItem = listOf(
        RssBottomNavigationItem(
            label = "Article", icon = RssViewerIcons.Article, route = RouteNavigation.ROUTE_ARTICLE,
        ), RssBottomNavigationItem(
            label = "Bookmark",
            icon = RssViewerIcons.Bookmarks,
            route = RouteNavigation.ROUTE_BOOKMARK
        ), RssBottomNavigationItem(
            label = "Categories",
            icon = RssViewerIcons.Category,
            route = RouteNavigation.ROUTE_CATEGORIES
        ), RssBottomNavigationItem(
            label = "Saved", icon = RssViewerIcons.Saved, route = RouteNavigation.ROUTE_SAVED
        )
    )
}
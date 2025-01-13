package ru.paylab.feature.infonavbar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun InformationNavigationBar(navController: NavController) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        val viewModel: InformationViewModel = hiltViewModel<InformationViewModel>()
        val articleCount = viewModel.articleCount.collectAsStateWithLifecycle()
        val articleBookmarkCount = viewModel.articleBookmarkCount.collectAsStateWithLifecycle()
        val categoriesFavoriteCount = viewModel.categoriesFavoriteCount.collectAsStateWithLifecycle()
        val savedArticles = viewModel.savedArticles.collectAsStateWithLifecycle()
        val listFunctions: List< @Composable ((String, ImageVector) -> Unit)> = listOf(
            { label, icon -> InformationIconBadge(articleCount.value,label, icon ) },
            { label, icon -> InformationIconBadge(articleBookmarkCount.value,label, icon ) },
            { label, icon -> InformationIconBadge(categoriesFavoriteCount.value,label, icon ) },
            { label, icon -> InformationIconBadge(savedArticles.value,label, icon ) },
        )

        BottomNavigationItems.RssBottomNavigationItem.forEachIndexed { index, navItem ->
            NavigationBarItem(selected = currentRoute == navItem.route, onClick = {
                navController.navigate(navItem.route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }, icon =  {
                if( listFunctions.size>index ) {
                    listFunctions[index].invoke(navItem.label, navItem.icon)
                } else {
                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                }
            }, label = {
                Text(
                    text = navItem.label,
                    softWrap = false,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            })
        }
    }
}
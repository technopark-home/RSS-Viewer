package ru.paylab.core.designsystem.uikit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import ru.paylab.core.designsystem.utils.RssViewerIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RssSearchTopBar(
    expanded: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onSearchQuery: (String) -> Unit,
    onExpanded: (Boolean) -> Unit,
    content: @Composable (ColumnScope.() -> Unit),
) {
    val textFieldState = rememberTextFieldState()
    SearchBar(
        //DockedSearchBar(
        modifier = modifier.imePadding(),
        inputField = {
            SearchBarDefaults.InputField(
                query = textFieldState.text.toString(),
                onQueryChange = {
                    textFieldState.setTextAndPlaceCursorAtEnd(it)
                    println("onQueryChange $it")
                    onSearchQuery(textFieldState.text.toString())
                },
                onSearch = {
                    textFieldState.setTextAndPlaceCursorAtEnd("")
                    onExpanded(false)
                    println("onSearch ${it}")
                },
                expanded = expanded.value,
                onExpandedChange = {
                    onExpanded(it)
                    onSearchQuery(textFieldState.text.toString())
                    println("onExpandedChange InputField ${it}")
                },
                placeholder = { Text("Enter search text") },
                leadingIcon = {
                    Icon(
                        RssViewerIcons.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            // Todo It bee together
                            onExpanded(false)
                            onSearchQuery("")
                        })
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (textFieldState.text.isNotEmpty()) {
                                textFieldState.setTextAndPlaceCursorAtEnd("")
                            }
                            else
                                onExpanded(false)
                        },
                    ) {
                        Icon(
                            imageVector = RssViewerIcons.Clear,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
            )
        },
        expanded = expanded.value,
        onExpandedChange = {
            onExpanded(it)
            println("onExpandedChange SearchBarArticles ${it}")
        },
        content = content,
    )
}
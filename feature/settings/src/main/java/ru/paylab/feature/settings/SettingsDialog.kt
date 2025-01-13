package ru.paylab.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.paylab.core.model.data.ColorMode
import ru.paylab.core.model.data.FilterView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    onCloseSettings: () -> Unit,
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BasicAlertDialog(onCloseSettings) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            when (val settingsState: SettingsUiState = uiState) {
                SettingsUiState.Loading -> Text(
                    text = "Loading...",
                )
                SettingsUiState.Error -> Text(
                    text = "Error...",
                )

                is SettingsUiState.Success -> {
                    val isForYouEnabled: MutableState<Boolean> = remember {
                        mutableStateOf(!settingsState.showByCategory)
                    }
                    val scrollState = rememberScrollState()
                    Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)) {
                        Text(
                            text = stringResource(R.string.settings),
                            style = MaterialTheme.typography.titleLarge,
                        )
                        HorizontalDivider(modifier = Modifier.height(1.dp))
                        enumValues<ColorMode>().forEach{ colorMode ->
                            SelectorBoolean(
                                selected = settingsState.colorMode == colorMode,
                                onClick = {
                                    viewModel.saveColorMode(colorMode)
                                },
                                text = colorMode.descriptor,
                            )
                        }
                        HorizontalDivider(modifier = Modifier.height(1.dp))
                        SelectorBoolean(
                            selected = settingsState.useDynamicColor,
                            onClick = {
                                viewModel.saveDynamicColor(!settingsState.useDynamicColor)
                            },
                            text = "Use dynamic Theme",
                        )
                        HorizontalDivider(modifier = Modifier.height(1.dp))
                        Text(
                            text = "Visual representation of data",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        )
                        SelectorBoolean(
                            selected = settingsState.showByCategory,
                            onClick = {
                                isForYouEnabled.value = settingsState.showByCategory
                                if (!isForYouEnabled.value) {
                                    val filterSet = settingsState.filterSet.toMutableSet()
                                    filterSet.remove(FilterView.FOR_YOU)
                                    viewModel.saveFilterSet(filterSet)
                                }
                                viewModel.saveShowByCategory(!settingsState.showByCategory)
                            },
                            text = "Show by category",
                        )
                        listOf(
                            FilterView.ALL,
                            //FilterView.UNREAD,
                            FilterView.FOR_YOU,
                        ).forEach { enumValue ->
                            SelectorFilter(
                                element = enumValue,
                                filterViews = settingsState.filterSet,
                                onSave = { viewModel.saveFilterSet(it) },
                                enabled = if (enumValue == FilterView.FOR_YOU) isForYouEnabled.value else true,
                                text = enumValue.descriptor,
                            )
                        }
                        Text(
                            text = "URL",
                            style = MaterialTheme.typography.labelMedium,
                        )
                        DropDownMenu(
                            viewModel.getURLs(),
                            viewModel.getIndexURL(settingsState.url),
                            viewModel::setURLs,
                        )
                        Text(
                            text = "Delete after",
                            style = MaterialTheme.typography.labelMedium,
                        )
                        val dateList = listOf("10", "20", "30")
                        DropDownMenu(
                            options = dateList,
                            index = dateList.indexOf(settingsState.deleteOldData.toString()),
                            onSave = { dateDelete ->
                                viewModel.saveDeleteDate(dateDelete.toLong())
                            },
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        TextButton(
                            onClick = onCloseSettings, modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(stringResource(R.string.confirm))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropDownMenu(
    options: List<String>,
    index: Int,
    onSave: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItemIndex by remember {
        mutableIntStateOf(index)
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            value = options[selectedItemIndex],
            onValueChange = {},
            readOnly = true,
            trailingIcon = { TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false }) {
            options.forEachIndexed { index, item ->
                DropdownMenuItem(text = {
                    Text(
                        text = item,
                        fontWeight = if (index == selectedItemIndex) FontWeight.Bold else null
                    )
                }, onClick = {
                    selectedItemIndex = index
                    onSave(options[selectedItemIndex])
                    expanded = false
                })
            }
        }
    }

}

@Composable
internal fun SelectorSwitch(
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    text: String,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .toggleable(
                role = Role.Switch,
                enabled = enabled,
                value = selected,
                onValueChange = { onClick() },
            )
    ) {
        Text(text)
        Spacer(Modifier.weight(1f))
        Switch(checked = selected, enabled = enabled, onCheckedChange = null)
    }
}

@Composable
internal fun SelectorFilter(
    element: FilterView,
    filterViews:  Set<FilterView>,
    onSave: (Set<FilterView>) -> Unit,
    enabled: Boolean,
    text: String,
) {
    SelectorSwitch(
        selected = filterViews.contains(element),
        onClick = {
            var saveSettings = filterViews.toMutableSet()
            saveSettings.apply {
                if (contains(element)) remove(element)
                else add(element)
            }
            saveSettings =
                saveSettings.sortedBy { filter -> filter.ordinal }.toMutableSet()
            onSave(saveSettings)
        },
        enabled = enabled,
        text = text,
    )
}

@Composable
internal fun SelectorBoolean(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}
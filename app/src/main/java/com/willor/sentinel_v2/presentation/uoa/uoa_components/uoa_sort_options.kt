package com.willor.sentinel_v2.presentation.uoa.uoa_components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.willor.lib_data.domain.dataobjs.UoaFilterOptions
import com.willor.sentinel_v2.ui.theme.MySizes


private val sortByRowHeight = 50.dp

@Composable
fun UoaSortOptions(
    uoaScreenStateProvider: () -> UoaScreenState,
    onFilterOptionClicked: (UoaFilterOptions) -> Unit,
    onSortAsc: (Boolean) -> Unit
){

    val uoaState = uoaScreenStateProvider()
    val curSortedByOption = uoaState.sortBy
    val ascending = uoaState.sortAsc

    var dropdownMenuExpanded by remember{ mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(sortByRowHeight),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){

        Text(
            text = "Sorted By:",
            style = MaterialTheme.typography.titleMedium
        )

        Box{

            ClickableFilterRow(
                curSelectedOption = curSortedByOption.name.replace('_', ' '),
                sortAsc = ascending,
                onSortByClicked = {
                    dropdownMenuExpanded = !dropdownMenuExpanded
                },
                onSortAscArrowClicked = onSortAsc
            )

            UoaFilterOptionsDropdownMenu(
                expanded = dropdownMenuExpanded,
                onOptionClicked = { filterOption: UoaFilterOptions ->
                    onFilterOptionClicked(filterOption)
                    dropdownMenuExpanded = false
                },
                onDismissRequest = { dropdownMenuExpanded = false }
            )

        }
    }
}


@Composable
private fun ClickableFilterRow(
    curSelectedOption: String,
    sortAsc: Boolean,
    onSortByClicked: () -> Unit,
    onSortAscArrowClicked: (Boolean) -> Unit
){
    val arrow = if (sortAsc){
        Icons.Filled.ArrowUpward
    }else{
        Icons.Filled.ArrowDownward
    }

    Row(
        modifier = Modifier
            .width(
                (LocalConfiguration.current.screenWidthDp * .6f).dp
            )
            .height(sortByRowHeight),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ){

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .clickable {
                    onSortByClicked()
                },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier.fillMaxWidth(.6f),
                text = curSelectedOption,
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                imageVector = Icons.Filled.FilterList,
                contentDescription = "filter-uoa",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.width(MySizes.HORIZONTAL_CONTENT_PADDING_SMALL))

        // Icon Group
        IconButton(
            onClick = {
                onSortAscArrowClicked(!sortAsc)
            }
        ) {
            Icon(
                imageVector = arrow,
                contentDescription = "sort_asc_sort_dsc",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


@Composable
private fun UoaFilterOptionsDropdownMenu(
    expanded: Boolean,
    onOptionClicked: (UoaFilterOptions) -> Unit,
    onDismissRequest: () -> Unit
){

    DropdownMenu(
        modifier = Modifier.wrapContentSize(),
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ){

        // Create a 'DropdownMenuItem' for each UoaFilterOption
        UoaFilterOptions.values().forEach {option: UoaFilterOptions ->

            DropdownMenuItem(
                onClick = {
                    onOptionClicked(option)
                }
            ) {

                Text(
                    text = option.name.replace('_', ' '),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}































package com.willor.sentinel_v2.presentation.uoa.uoa_components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items


@Composable
fun UoaList(
    uoaScreenStateProvider: () -> UoaScreenState
){

    val uiState = uoaScreenStateProvider()
    val pagingState = uiState.uoaPagingFlow?.collectAsLazyPagingItems()
    val sortedBy = uiState.sortBy

    pagingState?.let { lazyPagingItems ->

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){

            items(lazyPagingItems){uoa ->
                uoa?.let {
                    UoaExpandableCard(data = it, sortedBy = sortedBy, onSearchClicked = {})
                }
            }
        }
    }

}

















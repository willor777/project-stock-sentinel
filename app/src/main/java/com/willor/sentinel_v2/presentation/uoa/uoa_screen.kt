package com.willor.sentinel_v2.presentation.uoa

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.lib_data.domain.dataobjs.UoaFilterOptions
import com.willor.sentinel_v2.presentation.common.*
import com.willor.sentinel_v2.presentation.uoa.uoa_components.*
import com.willor.sentinel_v2.ui.theme.MySizes


/*
TODO
    - Include Multiple "Sort By" options
    - Include a "Search for ticker bar"
    - ITM or OTM should be highlighted / emboldened
    - Implied Volatility should also be emboldened
    - Ask and Bid should be at the bottom of the card
    - An "add to watchlist" icon option should be added to the UOA cards
    - Add a couple small floating action buttons...
        - 1. Scroll to top (Only visible once scrolled down)
        - 2. Expand-All/Collapse-All
 */

@Destination
@Composable
fun UoaScreen(
    navigator: DestinationsNavigator,
    viewmodel: UoaViewModel = hiltViewModel()
) {
    viewmodel.handleEvent(UoaScreenEvent.InitialLoad)

    val uiState by viewmodel.uiState.collectAsState()

    UoaScreenMain(
        uiStateProvider = { uiState },
        navDrawerDestinationClicked = {
            navController(navigator, it)
        },
        settingsIconClicked = {
            navController(navigator, Screens.Settings)
        },
        removeFromWatchlistClicked = {
            viewmodel.handleEvent(UoaScreenEvent.RemoveFromSentinelWatchlistClicked(it))
        },
        watchlistCardClicked = {
            navController(navigator, Screens.Quotes, it)
        },
        uoaFilterOptionClicked = {
            viewmodel.handleEvent(UoaScreenEvent.SortByOptionClicked(it))
        },
        uoaSortAscClicked = {
            viewmodel.handleEvent(UoaScreenEvent.SortAscDscClicked(it))
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UoaScreenMain(
    uiStateProvider: () -> UoaScreenState,
    navDrawerDestinationClicked: (Screens) -> Unit,
    settingsIconClicked: () -> Unit,
    removeFromWatchlistClicked: (ticker: String) -> Unit,
    watchlistCardClicked: (ticker: String) -> Unit,
    uoaFilterOptionClicked: (filterOption: UoaFilterOptions) -> Unit,
    uoaSortAscClicked: (sortAsc: Boolean) -> Unit,
    ) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState(drawerState)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            BasicTopBar(
                name = "Unusual Options Volume",
                onNavIconClick = {
                    openNavDrawer(coroutineScope, drawerState)
                },
                onSettingsIconClick = {
                    settingsIconClicked()
                }
            )
        },
        bottomBar = {},             // TODO Might not use this
        floatingActionButton = {},
        drawerContent = {
            NavDrawer(
                currentDestination = Screens.UnusualOptionsActivity,
                destinationClicked = { navDrawerDestinationClicked(it) }
            )
        },
        drawerGesturesEnabled = true
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ){                                                              // Main Column

            Column(
                modifier = Modifier
                    .fillMaxHeight(.92f)
                    .fillMaxWidth()
                    .padding(
                        horizontal = MySizes.HORIZONTAL_EDGE_PADDING,
                        vertical = 0.dp
                    )
            ){                                                          // Uoa List Column

                UoaSortOptions(
                    uoaScreenStateProvider = uiStateProvider,
                    onFilterOptionClicked = uoaFilterOptionClicked,
                    onSortAsc = uoaSortAscClicked
                )

                Divider()

                Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_MEDIUM))

                UoaColumnHeaderRow(uiStateProvider)

                Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_MEDIUM))

                UoaList(
                    uoaScreenStateProvider = uiStateProvider
                )

            }                                                           // Uoa List Column End

            SentinelWatchlistLazyrow(
                tickersList = {
                    uiStateProvider().userPrefs
                },
                onRemoveIconClicked = removeFromWatchlistClicked,
                onCardClicked = watchlistCardClicked
            )
        }                                                               // Main Column End
    }                       // --- Scaffold Scope End
}























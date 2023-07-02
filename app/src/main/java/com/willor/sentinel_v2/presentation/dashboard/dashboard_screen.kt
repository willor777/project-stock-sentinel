@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.willor.sentinel_v2.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.sentinel_v2.presentation.common.*
import com.willor.sentinel_v2.presentation.dashboard.dash_components.DashCollapsableTopBar
import com.willor.sentinel_v2.presentation.dashboard.dash_components.DashPopularWl
import com.willor.sentinel_v2.presentation.dashboard.dash_components.DashboardUiEvent
import com.willor.sentinel_v2.presentation.dashboard.dash_components.DashboardUiState
import com.willor.sentinel_v2.ui.theme.MySizes

//@RootNavGraph(start = true)
@Destination
@Composable
fun DashboardScreen(
    navigator: DestinationsNavigator,
    viewModel: DashboardViewModel = hiltViewModel()
) {

    val init_load_once = remember{ viewModel.handleEvent(DashboardUiEvent.InitialLoad) }

    val uiState by viewModel.uiState.collectAsState()

    DashboardScreenContent(
        uiStateProvider = { uiState },
        navDrawerDestinationClicked = {
            navController(navigator = navigator, destination = it)
        },
        onSettingsIconClicked = {
            navController(navigator, Screens.Settings)
        },
        onTickerCardClicked = {
            navController(navigator, Screens.Quotes, ticker = it)
        },
        onWlOptionClicked = viewModel::handleEvent,
        onAddTickerClick = viewModel::handleEvent,
        onRemoveIconClick = viewModel::handleEvent
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenContent(
    uiStateProvider: () -> DashboardUiState,
    navDrawerDestinationClicked: (Screens) -> Unit,
    onSettingsIconClicked: () -> Unit,
    onTickerCardClicked: (ticker: String) -> Unit,
    onWlOptionClicked: (event: DashboardUiEvent) -> Unit,
    onAddTickerClick: (event: DashboardUiEvent) -> Unit,
    onRemoveIconClick: (event: DashboardUiEvent) -> Unit,
) {

    val watchlistScrollState = rememberLazyListState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState(drawerState)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            DashCollapsableTopBar(
                dashUiStateProvider = uiStateProvider,
                dashScrollState = { watchlistScrollState },
                onNavIconClick = {
                    openNavDrawer(coroutineScope, drawerState)
                },
                onSettingsIconClick = onSettingsIconClicked,
                onTickerCardClicked = onTickerCardClicked,
            )
        },
        bottomBar = {},             // TODO Might not use this
        floatingActionButton = {},
        drawerContent = {
            NavDrawer(
                currentDestination = Screens.Dashboard,
                destinationClicked = { navDrawerDestinationClicked(it) }
            )
        },
        drawerGesturesEnabled = true
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.9f)
                    .padding(
                        MySizes.VERTICAL_CONTENT_PADDING_SMALL,
                        MySizes.HORIZONTAL_EDGE_PADDING
                    )
            ) {

                DashPopularWl(
                    dashboardUiStateProvider = uiStateProvider,
                    wlDispScrollStateProvider = { watchlistScrollState },
                    onTickerCardClicked = onTickerCardClicked,
                    onWlOptionClicked = {
                        onWlOptionClicked(DashboardUiEvent.WatchlistOptionClicked(it))
                    },
                    onAddTickerClick = {
                        onAddTickerClick(DashboardUiEvent.AddTickerToSentinelWatchlist(it))
                    }
                )
            }

            SentinelWatchlistLazyrow(
                tickersList = {
                      uiStateProvider().userPrefs
                },
                onRemoveIconClicked = {
                    onRemoveIconClick(DashboardUiEvent.RemoveTickerFromSentinelWatchlist(it))
                },
                onCardClicked = onTickerCardClicked
            )

        }       // --- Root Column Content Scope End
    }                       // --- Scaffold Scope End
}





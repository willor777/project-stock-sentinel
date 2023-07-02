package com.willor.sentinel_v2.presentation.quote

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.sentinel_v2.presentation.common.*
import com.willor.sentinel_v2.presentation.quote.quote_components.*
import com.willor.sentinel_v2.ui.theme.MySizes


/*
TODO
    - Quote Screen...
        - When new data is loading, Display a loading screen until all DataStates are either
        Success or Error. If any are still in the 'Loading' state, the load screen should stay.
        - Maybe consider making the text a little larger on the 'label value row's'
        - Maybe add a little 'i' info button next to some of the more arcane labels. Such as...
        Vol / Avg Vol Ratio, EPS, Beta, PE Ratio, all the complicated stats, etc...
        - Add an "Add Ticker" button somewhere to the quote screen.
    - Quote Competitors...
        - Competitors should be clickable...Should bring up their quote.
        - Maybe make the Competitors section 'cards with elevation' or maybe just draw a
        border around them
 */


val tag = "QUOTE_SCREEN"

@Destination
@Composable
fun QuoteScreen(
    ticker: String? = null,
    navigator: DestinationsNavigator,
    viewModel: QuoteViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsState()

    viewModel.handleUiEvent(QuoteUiEvent.InitialLoad(ticker))

    QuoteContent(
        quoteUiStateProvider = {
            uiState.value
        },
        navDrawerDestinationClicked = {
            navController(navigator, it)
        },
        onSettingsIconClicked = {
            navController(navigator, Screens.Settings)
        },
        onSearchBarTextChange = {
            viewModel.handleUiEvent(QuoteUiEvent.SearchTextUpdated(it))
        },
        onSearchResultClicked = {
            viewModel.handleUiEvent(QuoteUiEvent.SearchResultClicked(it))
        },
        onAddTickerToWatchlistClicked = {
            viewModel.handleUiEvent(QuoteUiEvent.AddTickerToSentinelWatchlist(it))
        },
        onRemoveFromWatchlistClicked = {
            viewModel.handleUiEvent(QuoteUiEvent.RemoveTickerFromSentinelWatchlist(it))
        },
        loadNewQuoteClicked = {
            viewModel.handleUiEvent(QuoteUiEvent.WatchlistTickerClicked(it))
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuoteContent(
    quoteUiStateProvider: () -> QuoteUiState,
    navDrawerDestinationClicked: (Screens) -> Unit,
    onSettingsIconClicked: () -> Unit,
    onSearchBarTextChange: (usrText: String) -> Unit,
    onSearchResultClicked: (ticker: String) -> Unit,
    onAddTickerToWatchlistClicked: (ticker: String) -> Unit,
    onRemoveFromWatchlistClicked: (ticker: String) -> Unit,
    loadNewQuoteClicked: (ticker: String) -> Unit,
) {

    val navDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState(drawerState = navDrawerState)
    val coroutineScope = rememberCoroutineScope()
    val contentScrollState = rememberScrollState()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            QuoteTopBar(
                onNavIconClick = {
                    openNavDrawer(coroutineScope, navDrawerState)
                },
                onSettingsIconClick = onSettingsIconClicked
            )
        },
        drawerContent = {
            NavDrawer(currentDestination = Screens.Quotes) {
                navDrawerDestinationClicked(it)
            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Search bar with drop down search results LazyCol
            TickerSearchBar(
                quoteUiStateProvider = quoteUiStateProvider,
                onTextChange = onSearchBarTextChange,
                onSearchResultClicked = onSearchResultClicked
            )

            // Scrollable Content (Stock/Etf Quote + Options Overview)
            Column(
                modifier = Modifier
                    .fillMaxHeight(.92f)
                    .fillMaxWidth()
                    .verticalScroll(contentScrollState)
                    .padding(
                        horizontal = MySizes.HORIZONTAL_EDGE_PADDING,
                        vertical = 0.dp
                    )
            ) {

                QuoteMainContent(
                    quoteUiStateProvider = quoteUiStateProvider,
                )

                Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE))

                QuoteOptionsOverview(
                    quoteUiStateProvider = quoteUiStateProvider
                )

                Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE))

                QuoteCompetitors(quoteUiStateProvider, loadNewQuoteClicked)

                QuoteSnrLevels(quoteUiStateProvider)

            }

            // TODO Make an "Add-To-Watchlist" button

            SentinelWatchlistLazyrow(
                tickersList = {
                    quoteUiStateProvider().userPrefs
                },
                onRemoveIconClicked = onRemoveFromWatchlistClicked,
                onCardClicked = loadNewQuoteClicked
            )

        }
    }
}























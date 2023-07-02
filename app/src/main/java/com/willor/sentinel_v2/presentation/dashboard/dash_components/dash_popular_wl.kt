package com.willor.sentinel_v2.presentation.dashboard.dash_components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.popular_wl_resp.PopularWatchlist
import com.willor.lib_data.domain.dataobjs.responses.popular_wl_resp.Ticker
import com.willor.sentinel_v2.presentation.common.DropdownOptionSelector
import com.willor.sentinel_v2.presentation.common.LabelValueRow
import com.willor.sentinel_v2.ui.theme.GainGreen
import com.willor.sentinel_v2.ui.theme.LossRed
import com.willor.sentinel_v2.ui.theme.MySizes
import com.willor.sentinel_v2.utils.calculateRatio
import com.willor.sentinel_v2.utils.determineGainLossColor
import com.willor.sentinel_v2.utils.formatChangeDollarAndChangePct
import com.willor.sentinel_v2.utils.formatDoubleToTwoDecimalPlaceString
import kotlinx.coroutines.launch


@Composable
fun DashPopularWl(
    dashboardUiStateProvider: () -> DashboardUiState,
    wlDispScrollStateProvider: () -> LazyListState,
    onTickerCardClicked: (ticker: String) -> Unit,
    onWlOptionClicked: (wlName: String) -> Unit,
    onAddTickerClick: (ticker: String) -> Unit
) {

    val dashboardUiState = dashboardUiStateProvider()

    var wlOptionsLoaded: List<String>? by remember {
        mutableStateOf(null)
    }
    var curWatchlistLoaded: PopularWatchlist? by remember {
        mutableStateOf(null)
    }

    val wlOptionsState = dashboardUiState.popularWatchlistOptions
    val selectedWlState = dashboardUiState.popularWatchlistDisplayed

    when (wlOptionsState) {
        is DataResourceState.Success -> {
            wlOptionsLoaded = wlOptionsState.data!!.data
        }
    }

    when (selectedWlState) {
        is DataResourceState.Success -> {
            curWatchlistLoaded = selectedWlState.data!!
        }
    }

    if (wlOptionsLoaded != null && curWatchlistLoaded != null) {
        WatchlistDisplay(
            displayData = WatchlistDisplayData(
                wlOptionsLoaded!!, curWatchlistLoaded!!, wlDispScrollStateProvider
            ),
            onWlOptionClicked = onWlOptionClicked,
            onTickerCardClicked = onTickerCardClicked,
            onAddTickerClick = onAddTickerClick
        )
    }

}


@Stable
data class WatchlistDisplayData(
    val options: List<String>,
    val curWatchlist: PopularWatchlist,
    val wlDispScrollStateProvider: () -> LazyListState
)

@Composable
private fun WatchlistDisplay(
    displayData: WatchlistDisplayData,
    onWlOptionClicked: (wlName: String) -> Unit,
    onTickerCardClicked: (ticker: String) -> Unit,
    onAddTickerClick: (ticker: String) -> Unit,
) {

    val wlScrollstate = rememberLazyListState()
    val cScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxHeight(1f)
            .fillMaxWidth()

    ) {
        // Watchlist selector
        DropdownOptionSelector(optionsList = displayData.options,
            curSelection = displayData.curWatchlist.watchlistData.name,
            onItemClick = {
                onWlOptionClicked(it)
                cScope.launch { wlScrollstate.animateScrollToItem(0) }
            })

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = displayData.wlDispScrollStateProvider(),
        ) {

            items(displayData.curWatchlist.watchlistData.tickers.size) { itemIndex ->
                ExpandableTickerCard(
                    ticker = displayData.curWatchlist.watchlistData.tickers[itemIndex],
                    onTickerCardClicked = onTickerCardClicked,
                    searchIconClicked = onAddTickerClick
                )
            }
        }
    }
}


@Composable
private fun ExpandableTickerCard(
    ticker: Ticker,
    onTickerCardClicked: (ticker: String) -> Unit,
    searchIconClicked: (ticker: String) -> Unit,
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val curSize by animateDpAsState(
        targetValue = if (expanded) 190.dp else 82.dp
    )

    val volRatio = calculateRatio(
        ticker.volume.toDouble(), ticker.volumeThirtyDayAvg.toDouble()
    )

    val volRatioColor = if (volRatio >= 1) {
        GainGreen
    } else {
        LossRed
    }

    Card(
        modifier = Modifier
            .padding(vertical = MySizes.VERTICAL_CONTENT_PADDING_SMALL)
            .clickable {
                expanded = !expanded
            },

        elevation = 2.dp,
//            border = BorderStroke(1.dp, color = Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(curSize)
                .fillMaxWidth(.9f)
                .padding(
                    MySizes.HORIZONTAL_CONTENT_PADDING_SMALL, MySizes.VERTICAL_CONTENT_PADDING_SMALL
                ),
            horizontalAlignment = Alignment.Start,
        ) {

            TickerCardHeader(
                tickerSymbol = ticker.ticker, onAddButtonClicked = searchIconClicked
            )

            LabelValueRow(label = "Company Name", value = ticker.companyName)

            LabelValueRow(
                label = "Days Change", value = formatChangeDollarAndChangePct(
                    ticker.changeDollar, ticker.changePercent
                ), valueColor = determineGainLossColor(ticker.changePercent)
            )
            LabelValueRow(
                label = "Last Price",
                value = "$ ${formatDoubleToTwoDecimalPlaceString(ticker.lastPrice)}"
            )

            LabelValueRow(
                label = "Volume", labelSuperScript = "Today", value = ticker.volume.toString()
            )

            LabelValueRow(
                label = "Avg Volume",
                labelSuperScript = "30day",
                value = ticker.volumeThirtyDayAvg.toString()
            )

            LabelValueRow(
                label = "Vol / Avg Vol ratio",
                value = formatDoubleToTwoDecimalPlaceString(volRatio),
                valueColor = volRatioColor
            )

            LabelValueRow(
                label = "Market Cap", value = "$ ${ticker.marketCapAbbreviatedString}"
            )

            SearchIconRow(onClick = { onTickerCardClicked(ticker.ticker) })
        }
    }
}


@Composable
private fun SearchIconRow(onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = { onClick() }) {
            Icon(
                Icons.Filled.Search,
                contentDescription = "search-ticker",
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(20.dp, 20.dp)
            )
        }
    }

}


@Composable
private fun TickerCardHeader(
    tickerSymbol: String,
    onAddButtonClicked: (ticker: String) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(22.dp)
            .background(MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // Header
        Text(
            text = tickerSymbol,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                horizontal = MySizes.HORIZONTAL_EDGE_PADDING, vertical = 0.dp
            )
        )

        IconButton(onClick = { onAddButtonClicked(tickerSymbol) }) {
            Icon(

                Icons.Filled.AddBox,
                "add-ticker-to-sentinel-watchlist",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(15.dp, 15.dp)
            )
        }
    }
}


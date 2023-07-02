package com.willor.sentinel_v2.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.willor.sentinel_v2.ui.theme.MySizes
import com.willor.sentinel_v2.utils.determineGainLossColor
import com.willor.sentinel_v2.utils.formatChangeDollarAndChangePct


@Composable
fun TickerCardLazyRow(
    tickerList: List<String>,
    gainDollarList: List<Double>,
    gainPctList: List<Double>,
    onItemClicked: (ticker: String) -> Unit
) {

    LazyRow(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(count = tickerList.lastIndex) { itemIndex ->
            TickerCardItem(
                ticker = tickerList[itemIndex],
                gainDollar = gainDollarList[itemIndex],
                gainPct = gainPctList[itemIndex],
                onCardClicked = {
                    onItemClicked(it)
                }
            )
        }
    }
}


@Composable
private fun TickerCardItem(
    ticker: String,
    gainDollar: Double,
    gainPct: Double,
    onCardClicked: (ticker: String) -> Unit
) {

    val glColor = determineGainLossColor(gainDollar)

    Card(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
                onCardClicked(ticker)
            },
        shape = RoundedCornerShape(MySizes.CARD_ROUNDED_CORNER),
        elevation = MySizes.CARD_ELEVATION,
        backgroundColor = MaterialTheme.colorScheme.secondary
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(MaterialTheme.colorScheme.secondary),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // Future Name
            Text(
                text = ticker,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                fontFamily = MaterialTheme.typography.titleSmall.fontFamily,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(
                    top = MySizes.VERTICAL_CONTENT_PADDING_SMALL,
                    bottom = 0.dp,
                    start = MySizes.HORIZONTAL_CONTENT_PADDING_MEDIUM,
                    end = MySizes.HORIZONTAL_CONTENT_PADDING_MEDIUM
                )
            )

            // Future Change
            Text(
                text = formatChangeDollarAndChangePct(gainDollar, gainPct),
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily,
                color = glColor,
                modifier = Modifier.padding(
                    top = 0.dp,
                    bottom = MySizes.VERTICAL_CONTENT_PADDING_SMALL,
                    start = MySizes.HORIZONTAL_CONTENT_PADDING_MEDIUM,
                    end = MySizes.HORIZONTAL_CONTENT_PADDING_MEDIUM
                )
            )
        }

    }
}



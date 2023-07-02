package com.willor.sentinel_v2.presentation.quote.quote_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.stock_competitors_resp.Competitor
import com.willor.lib_data.domain.dataobjs.responses.stock_competitors_resp.StockCompetitors
import com.willor.sentinel_v2.presentation.common.LabelValueRow
import com.willor.sentinel_v2.ui.theme.MySizes
import com.willor.sentinel_v2.utils.determineGainLossColor
import com.willor.sentinel_v2.utils.formatDoubleToTwoDecimalPlaceString


@Composable
fun QuoteCompetitors(
    quoteUiStateProvider: () -> QuoteUiState,
    onCompetitorClicked: (ticker: String) -> Unit
){

    val uiState = quoteUiStateProvider()
    val competitors = uiState.competitors
    val curTicker = uiState.currentTicker
    when(competitors){
        is DataResourceState.Success -> {
            CompetitorsContent(
                curTicker = curTicker,
                competitors = competitors.data,
                onCompetitorClicked = onCompetitorClicked
                )
        }
        else -> {

        }
    }
}


/**
 * The content for this composable.
 */
@Composable
private fun CompetitorsContent(
    curTicker: String,
    competitors: StockCompetitors?,
    onCompetitorClicked: (ticker: String) -> Unit
){
    competitors?.let{

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),

        ){

            // Header with text "{ticker}'s Top Competitors:
            Header(ticker = curTicker)

            // Spacer same size as main quote content & options overview
            Spacer(modifier = Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE))

            for (competitor in competitors.data.competitorList){
                CompetitorInfo(competitor = competitor, onCompetitorClicked = onCompetitorClicked)
            }
        }
    }
}


/**
 * Header for section of display. Text is "{ticker}'s Top Competitors
 */
@Composable
private fun Header(
    ticker: String
){
    Text(
        text = "$ticker's Top Competitors",
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
        fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        textAlign = TextAlign.Center
    )
}


/**
 * Displays the Ticker, Pct Change, Company Name, and Market cap of a "Competitor" object
 */
@Composable
private fun CompetitorInfo(
    competitor: Competitor,
    onCompetitorClicked: (ticker: String) -> Unit,
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCompetitorClicked(competitor.ticker)
            },
        verticalArrangement = Arrangement.Top,
    ){

        // Top row data containing ticker and pct change
        CompetitorInfoBigRowData(
            ticker = competitor.ticker,
            pctChange = competitor.pctChange
        )

        // Bottom row data containing company name and market cap
        CompetitorInfoSmallRowData(
            compName = competitor.companyName,
            marketCap = competitor.marketCapAbbreviatedString
        )
    }
}


/**
 * Top row data containing ticker and pct change in large bold text
 */
@Composable
private fun CompetitorInfoBigRowData(
    ticker: String,
    pctChange: Double
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){

        Text(
            text = ticker,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = MaterialTheme.colorScheme.onBackground
        )


        Text(
            text = "${formatDoubleToTwoDecimalPlaceString(pctChange)}%",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            color = determineGainLossColor(pctChange)
        )
    }
}


/**
 * Bottom row data containing company name and market cap in smallish text
 */
@Composable
private fun CompetitorInfoSmallRowData(
    compName: String,
    marketCap: String,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ){

        LabelValueRow(
            label = "Company:",
            value = compName
        )

        LabelValueRow(
            label = "Market Cap:",
            value = marketCap
        )
    }
}

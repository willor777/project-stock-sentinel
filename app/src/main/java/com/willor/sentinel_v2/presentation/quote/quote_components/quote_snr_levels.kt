package com.willor.sentinel_v2.presentation.quote.quote_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.dataobjs.responses.stock_snr_levels_resp.StockSnrLevels
import com.willor.sentinel_v2.presentation.common.LabelValueRow
import com.willor.sentinel_v2.ui.theme.MySizes


/*
TODO
    Make a section on the quote screen dedicated to
    SnR levels (if they are available of course).
 */


@Composable
fun QuoteSnrLevels(
    quoteUiStateProvider: () -> QuoteUiState
){

    val uiState = quoteUiStateProvider()
    val snrLevels = uiState.snrLevels

    when (snrLevels) {
        is DataResourceState.Success -> {
            SnrLevelsContent(snrLevels = snrLevels.data!!, uiState.currentTicker)
        }
    }
}


@Composable
private fun SnrLevelsContent(
    snrLevels: StockSnrLevels,
    ticker: String,
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){

        // Header
        SnrLevelsMainHeader(ticker = ticker)

        // Resistance Header
        LevelTypeHeader(header = "Resistance Levels")

        // R3, R2, R1
        SnrLevel(levelName = "Resistance Level 3", levelValue = snrLevels.data.r3.toString())
        SnrLevel(levelName = "Resistance Level 2", levelValue = snrLevels.data.r2.toString())
        SnrLevel(levelName = "Resistance Level 1", levelValue = snrLevels.data.r1.toString())

        LevelTypeHeader(header = "Support Levels")

        // S1, S2, S3
        SnrLevel(levelName = "Support Level 1", levelValue = snrLevels.data.s1.toString())
        SnrLevel(levelName = "Support Level 2", levelValue = snrLevels.data.s2.toString())
        SnrLevel(levelName = "Support Level 3", levelValue = snrLevels.data.s3.toString())

        Spacer(modifier = Modifier.height(MySizes.VERTICAL_EDGE_PADDING ))

    }
}


@Composable
private fun SnrLevelsMainHeader(
    ticker: String
) {
    Text(
        text = "$ticker S&R Levels",
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



@Composable
private fun SnrLevel(
    levelName: String,
    levelValue: String,
    levelSymbol: String = "$",
){

    // if symbol is a $ then it goes infront. If it is anything else (%) it goes behind
    val valueText = if (levelSymbol == "$"){
        "$$levelValue"
    }else{
        "$levelValue$levelSymbol"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){

        LabelValueRow(
            label = levelName,
            value = valueText
        )
    }
}


@Composable
private fun LevelTypeHeader(
    header: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        Text(
            text = header,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Bold,
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
            fontStyle = MaterialTheme.typography.bodyLarge.fontStyle
        )
    }
}